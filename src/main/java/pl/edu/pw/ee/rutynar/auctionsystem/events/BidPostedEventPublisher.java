package pl.edu.pw.ee.rutynar.auctionsystem.events;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Auction;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Bid;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.NotificationType;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.AuctionRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.services.NotificationService;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Component
public class BidPostedEventPublisher {


    private Map<ObjectId, BlockingQueue<BidPostedEvent>> auctionQueueMap = new HashMap<>();

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private NotificationService notificationService;

    public BlockingQueue<BidPostedEvent> getAuctionQueue(ObjectId id) {
        if (!this.auctionQueueMap.containsKey(id)) {
            this.auctionQueueMap.put(id, new LinkedBlockingQueue<>());
        }
        return this.auctionQueueMap.get(id);
    }

    @Async("threadPoolTaskExecutor")
    @EventListener
    public void onApplicationEvent(BidPostedEvent event) {
        auctionRepository.findAuctionByBids((Bid) event.getSource())
                .map(Auction::getId)
                .subscribe(id -> {
                    if (!this.auctionQueueMap.containsKey(id)) {
                        this.auctionQueueMap.put(id, new LinkedBlockingQueue<>());
                    }
                    this.auctionQueueMap.get(id).offer(event);
                    this.notifyAuctionFollowers(id);
                });
    }

    private void notifyAuctionFollowers(ObjectId auctionId){
        auctionRepository.findById(auctionId)
                .map(Auction::getFollowers)
                .flux()
                .flatMap(Flux::fromIterable)
                .doOnNext(user -> this.notificationService.createNotification(user, NotificationType.POSTED_BID))
                .subscribe();
    }
}
