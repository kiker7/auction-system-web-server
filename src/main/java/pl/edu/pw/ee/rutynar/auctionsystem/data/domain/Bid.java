package pl.edu.pw.ee.rutynar.auctionsystem.data.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document
public class Bid {

    @EqualsAndHashCode.Include
    @Id
    private ObjectId id;

    private int offer;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date requestTime;

    @DBRef
    private User user;
}
