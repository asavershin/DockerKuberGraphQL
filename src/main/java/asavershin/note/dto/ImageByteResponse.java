package asavershin.note.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageByteResponse {
    private String link;
    private byte[] bytes;
}
