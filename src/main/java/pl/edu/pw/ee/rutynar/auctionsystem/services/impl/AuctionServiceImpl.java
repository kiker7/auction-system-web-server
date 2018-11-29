package pl.edu.pw.ee.rutynar.auctionsystem.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Auction;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Bid;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.User;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.AuctionRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.BidRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.events.BidPostedEvent;
import pl.edu.pw.ee.rutynar.auctionsystem.events.BidPostedEventPublisher;
import pl.edu.pw.ee.rutynar.auctionsystem.services.AuctionService;
import pl.edu.pw.ee.rutynar.auctionsystem.services.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

@Slf4j
@Service
public class AuctionServiceImpl implements AuctionService {

    private ApplicationEventPublisher publisher;
    private BidRepository bidRepository;
    private AuctionRepository auctionRepository;
    private UserService userService;
    private BidPostedEventPublisher eventPublisher;
    private Map<ObjectId, Flux<Bid>> publishersMap;

    @Autowired
    private Executor executor;


    public AuctionServiceImpl(ApplicationEventPublisher publisher, BidRepository bidRepository, AuctionRepository auctionRepository, UserService userService, BidPostedEventPublisher eventPublisher) {
        this.publisher = publisher;
        this.bidRepository = bidRepository;
        this.auctionRepository = auctionRepository;
        this.userService = userService;
        this.eventPublisher = eventPublisher;
        this.publishersMap = new HashMap<>();
    }

    private Flux<Bid> getAuctionBidsPublisher(ObjectId id) {
        if (!this.publishersMap.containsKey(id)) {
            Flux<BidPostedEvent> emitter = Flux
                    .create(fluxSink -> this.executor.execute(() -> {
                        while (true) {
                            try {
                                BidPostedEvent event = this.eventPublisher.getAuctionQueue(id).take();
                                fluxSink.next(event);
                            } catch (InterruptedException e) {
                                ReflectionUtils.rethrowRuntimeException(e);
                            }
                        }
                    }));
            Flux<Bid> publisher = emitter
                    .map(event -> (Bid) event.getSource())
                    .doOnSubscribe(s -> log.debug("OnSubscribe AUCTION: " + id))
                    .doOnComplete(() -> log.debug("OnComplete AUCTION: " + id))
                    .doOnCancel(() -> log.debug("OnCancel AUCTION: " + id))
                    .publish().refCount(1, Duration.ofDays(1));
            this.publishersMap.put(id, publisher);
        }
        return this.publishersMap.get(id);
    }

    @Override
    public Mono<Mono<Auction>> addBidToAuction(Mono<Bid> bidMono, ObjectId auctionId) {

        Mono<Auction> auctionMono = auctionRepository.findById(auctionId);
        Mono<User> userMono = userService.getCurrentUser();

        return bidMono
                .zipWith(userMono, (bid, user) -> {
                    bid.setUser(user);
                    bid.setRequestTime(new Date());
                    return bidRepository.save(bid);
                })
                .flatMap(bids -> bids.map(bid -> bid))
                .zipWith(auctionMono, (bid, auction) -> {
                    if (auction.getBids() == null) {
                        auction.setBids(new ArrayList<>());
                    }
                    auction.getBids().add(bid);
                    return auctionRepository.save(auction).doOnSuccess(auction1 -> this.publisher.publishEvent(new BidPostedEvent(bid)));
                });
    }


    /**
     * Return Flux with all bids from auction and concat new posted bids, used for SSE
     */
    @Override
    public Flux<Bid> getAuctionPublishedBids(ObjectId auctionId) {

        Mono<Auction> auctionMono = auctionRepository.findById(auctionId);
        return auctionMono
                .map(Auction::getBids)
                .flux()
                .flatMap(Flux::fromIterable)
                .concatWith(getAuctionBidsPublisher(auctionId));
    }
}
