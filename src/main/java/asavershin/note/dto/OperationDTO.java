package asavershin.note.dto;

import asavershin.note.entities.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationDTO {
    private String id;
    private String content;
    private String dateTime;
    private String type;
}
