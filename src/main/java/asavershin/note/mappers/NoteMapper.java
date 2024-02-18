package asavershin.note.mappers;

import asavershin.note.dto.NoteDTO;
import asavershin.note.entities.NoteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NoteMapper extends EntityDTOMapper<NoteEntity, NoteDTO> {
    @Mapping(target = "noteId", source = "noteId")
    NoteEntity toEntity(Long noteId,
            NoteDTO dto
    );
}
