package asavershin.note.repositories;

import asavershin.note.entities.ImageEntity;

import java.util.List;
import java.util.Optional;

public interface ImageRepository{
    List<ImageEntity> deleteAllByNoteId(Long noteId);
    ImageEntity save(ImageEntity file);
    boolean deleteById(Long id);

    List<String> findLinksByNoteId(Long noteId);

    boolean existById(Long id);

    Optional<ImageEntity> findById(Long id);
}
