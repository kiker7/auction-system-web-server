package pl.edu.pw.ee.rutynar.auctionsystem.events;

import org.springframework.context.ApplicationEvent;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Bid;

public class BidPostedEvent extends ApplicationEvent {

    public BidPostedEvent(Bid source) {
        super(source);
    }
}
