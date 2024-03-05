package asavershin.note.repositories;

import asavershin.note.entities.NoteEntity;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends CrudRepository<NoteEntity, Long> {
    boolean existById(Long id);

    List<NoteEntity> findAll(Long pageNumber, Long pageSize);

    Optional<NoteEntity> findByIdWithImages(Long id);

    Optional<NoteEntity> findByImageId(Long id);
}
