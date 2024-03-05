package asavershin.note.services;

import asavershin.note.dto.ImageByteResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    void uploadFile(Long noteId,Long userId, MultipartFile image);
    void deleteById(Long id, Long userId);

    @Transactional
    List<ImageByteResponse> downloadImages(List<String> images);
}
