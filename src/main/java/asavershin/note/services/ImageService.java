package asavershin.note.services;

import asavershin.note.dto.ImageByteResponse;
import asavershin.note.exceptions.EntityNotFoundException;
import asavershin.note.exceptions.FileException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    void uploadFile(Long noteId, MultipartFile image);
    void deleteById(Long id);

    @Transactional
    List<ImageByteResponse> downloadImages(List<String> images);
}
