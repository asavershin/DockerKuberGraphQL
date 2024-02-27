package asavershin.note.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class NoteDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long noteId;
    private String noteHeader;
    private String noteMessage;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String noteCreatedAt;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String noteLastUpdatedAt;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<ImageDTO> images;
}
