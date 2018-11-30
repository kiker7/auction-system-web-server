package pl.edu.pw.ee.rutynar.auctionsystem.data.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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

    @JsonSerialize(using = ToStringSerializer.class)
    @EqualsAndHashCode.Include
    @Id
    private ObjectId id;

    @JsonIgnore
    @DBRef
    private User recipient;

    private String message;
}
