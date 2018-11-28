package pl.edu.pw.ee.rutynar.auctionsystem.services.impl;

import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Auction;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Bid;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.User;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.AuctionRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.BidRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.events.BidPostedEvent;
import pl.edu.pw.ee.rutynar.auctionsystem.services.AuctionService;
import pl.edu.pw.ee.rutynar.auctionsystem.services.UserService;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;

@Service
public class AuctionServiceImpl implements AuctionService {

    private ApplicationEventPublisher publisher;
    private BidRepository bidRepository;
    private AuctionRepository auctionRepository;
    private UserService userService;


    public AuctionServiceImpl(ApplicationEventPublisher publisher, BidRepository bidRepository, AuctionRepository auctionRepository, UserService userService) {
        this.publisher = publisher;
        this.bidRepository = bidRepository;
        this.auctionRepository = auctionRepository;
        this.userService = userService;
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
                    return auctionRepository.save(auction).doOnSuccess(auction1 -> this.publisher.publishEvent(new BidPostedEvent(auction1)));
                });
    }
}
