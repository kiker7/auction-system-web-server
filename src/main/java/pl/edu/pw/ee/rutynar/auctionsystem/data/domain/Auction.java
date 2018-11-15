package pl.edu.pw.ee.rutynar.auctionsystem.data.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document
public class Auction {

    @EqualsAndHashCode.Include
    @Id
    private ObjectId id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date closingTime;

    private boolean finished;

    @DBRef
    private User owner;

    @DBRef
    private List<Bid> bids;


    @DBRef
    private List<User> followers;
}
