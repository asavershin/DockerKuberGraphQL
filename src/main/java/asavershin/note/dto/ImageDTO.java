package asavershin.note.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long imageId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String imageName;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long imageSize;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String imageLink;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long noteId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MultipartFile image;
}
