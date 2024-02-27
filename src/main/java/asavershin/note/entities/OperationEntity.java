package asavershin.note.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Operation")
public class OperationEntity {
    @Id
    private String id;
    private String content;
    private LocalDateTime dateTime;
    private OperationType type;
}
