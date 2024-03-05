package asavershin.note.mappers;

import asavershin.note.dto.OperationDTO;
import asavershin.note.entities.OperationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OperationMapper extends EntityDTOMapper<OperationEntity, OperationDTO>{
}
