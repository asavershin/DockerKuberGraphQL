package asavershin.note.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageEntity implements Serializable {
    private Long imageId;
    private String imageName;
    private Long imageSize;
    private String imageLink;
    private Long noteId;
}
