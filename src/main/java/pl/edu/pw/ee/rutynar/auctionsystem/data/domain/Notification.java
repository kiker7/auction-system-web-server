package pl.edu.pw.ee.rutynar.auctionsystem.data.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document
public class Notification {

    @EqualsAndHashCode.Include
    @Id
    private ObjectId id;

    @DBRef
    private User recipient;

    private String message;
}
