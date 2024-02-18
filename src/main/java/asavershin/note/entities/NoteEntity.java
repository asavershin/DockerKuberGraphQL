package asavershin.note.entities;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoteEntity {
    private Long noteId;
    private String noteHeader;
    private String noteMessage;
    private LocalDateTime noteCreatedAt;
    private LocalDateTime noteLastUpdatedAt;
}
