package asavershin.note.repositories;

import asavershin.note.entities.ImageEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageRepository{
    List<ImageEntity> deleteAllByNoteId(Long noteId);
    ImageEntity insert(ImageEntity file);
    ImageEntity deleteById(Long id);

    List<String> findLinksByNoteId(Long noteId);

    boolean existById(Long id);
}
