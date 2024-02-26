package asavershin.note.mappers;

import asavershin.note.dto.ImageDTO;
import asavershin.note.entities.ImageEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMapper extends EntityDTOMapper<ImageEntity, ImageDTO>{

}
