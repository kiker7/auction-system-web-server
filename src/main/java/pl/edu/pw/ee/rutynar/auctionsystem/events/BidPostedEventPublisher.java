package pl.edu.pw.ee.rutynar.auctionsystem.events;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Bid;
import reactor.core.publisher.FluxSink;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Slf4j
@Component
public class BidPostedEventPublisher implements ApplicationListener<BidPostedEvent> , Consumer<FluxSink<BidPostedEvent>> {


    // queue Map: auctionId <-> queue
//    private Map<ObjectId, BlockingQueue<BidPostedEvent>> auctionQueueMap;


    private final Executor executor;
    private final BlockingQueue<BidPostedEvent> queue = new LinkedBlockingQueue<>();

    public BidPostedEventPublisher(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void onApplicationEvent(BidPostedEvent event) {
        log.info("EVENT: NEW BID");
        this.queue.offer(event);
    }

    @Override
    public void accept(FluxSink<BidPostedEvent> bidPostedEventFluxSink) {

        this.executor.execute(() ->{
            while (true){
                try{
                    BidPostedEvent event = queue.take();
                    bidPostedEventFluxSink.next(event);
                }catch (InterruptedException e){
                    ReflectionUtils.rethrowRuntimeException(e);
                }
            }
        });
    }
}
