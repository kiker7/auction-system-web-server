package pl.edu.pw.ee.rutynar.auctionsystem.events;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Auction;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Bid;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.AuctionRepository;

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

    public BlockingQueue<BidPostedEvent> getAuctionQueue(ObjectId id) {
        if (!this.auctionQueueMap.containsKey(id)) {
            this.auctionQueueMap.put(id, new LinkedBlockingQueue<>());
            log.debug("Created new Queue for auction: " + id);
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
                        log.debug("Created new queue for auction: " + id);
                    }
                    this.auctionQueueMap.get(id).offer(event);
                    log.debug("EVENT: " + event.getSource());
                });
    }
}
