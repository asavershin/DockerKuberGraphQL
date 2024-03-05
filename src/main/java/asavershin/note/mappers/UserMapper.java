package asavershin.note.mappers;

import asavershin.note.dto.UserDTO;
import asavershin.note.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityDTOMapper<UserEntity, UserDTO>{
}
