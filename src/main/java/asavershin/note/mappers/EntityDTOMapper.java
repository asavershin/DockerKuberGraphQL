package asavershin.note.mappers;

import java.util.List;

public interface EntityDTOMapper<T,K> {
    K toDto(
            T entity
    );

    List<K> toDto(
            List<T> entity
    );

    T toEntity(
            K dto
    );
}
