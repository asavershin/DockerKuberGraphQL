package asavershin.note.entities;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoteEntity implements Serializable {
    private Long noteId;
    private String noteHeader;
    private String noteMessage;
    private LocalDateTime noteCreatedAt;
    private LocalDateTime noteLastUpdatedAt;
    private List<ImageEntity> images;
}
