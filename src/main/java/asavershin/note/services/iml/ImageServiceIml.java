package asavershin.note.services.iml;

import asavershin.note.dto.ImageByteResponse;
import asavershin.note.entities.ImageEntity;
import asavershin.note.entities.NoteEntity;
import asavershin.note.entities.OperationEntity;
import asavershin.note.entities.OperationType;
import asavershin.note.exceptions.AuthException;
import asavershin.note.exceptions.EntityNotFoundException;
import asavershin.note.repositories.ImageRepository;
import asavershin.note.repositories.NoteRepository;
import asavershin.note.services.ImageService;
import asavershin.note.services.MinioService;
import asavershin.note.services.OperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceIml implements ImageService {
    private final MinioService minioService;
    private final ImageRepository imageRepository;
    private final NoteRepository noteRepository;
    private final CacheManager cacheManager;
    private final OperationService operationService;

    @Transactional
    @Override
    @CacheEvict(value = "NoteServiceIml::getEntityById", key = "#noteId")
    public void uploadFile(Long noteId,Long userId, MultipartFile image){
        if (!noteRepository.existById(noteId)) {
            throw new EntityNotFoundException("Note not found with id: " + noteId);
        }

        NoteEntity note = noteRepository.findById(noteId).orElseThrow(() -> new EntityNotFoundException("Note with not found with id: "+noteId));
        if(!note.getUserId().equals(userId)){
            throw new AuthException("You are trying to delete a resource that is not yours");
        }

        var imageEntity = new ImageEntity(
                null,
                image.getOriginalFilename(),
                image.getSize(),
                minioService.saveFile(image),
                noteId);
        try {
            imageEntity = imageRepository.save(imageEntity);
        } catch (Exception ex) {
            minioService.deleteFiles(Collections.singletonList(imageEntity.getImageLink()));
            throw ex;
        }
        operationService.logOperation(
                new OperationEntity(null,
                        String.format("Upload image: %s", imageEntity),
                        LocalDateTime.now(),
                        OperationType.WRITE)

        );
    }

    @Override
    @Transactional
    public void deleteById(Long id, Long userId){
        var deletedImage = imageRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Image not found with id: " + id));
        var note = noteRepository.findByImageId(id).orElseThrow(() -> new EntityNotFoundException("Note with image: " + id + " not found"));
        if(!note.getUserId().equals(userId)){
            throw new AuthException("You are trying to delete a resource that is not yours");
        }
        imageRepository.deleteById(id);
        minioService.deleteFiles(Collections.singletonList(deletedImage.getImageLink()));
        var cach = cacheManager.getCache("NoteServiceIml::getEntityById");
        cach.evict(note.getNoteId());
        operationService.logOperation(
                new OperationEntity(null,
                        String.format("Delete image : %s", deletedImage),
                        LocalDateTime.now(),
                        OperationType.DELETE)

        );
    }

    @Override
    @Transactional
    public List<ImageByteResponse> downloadImages(List<String> images){
        return images.stream()
                .map(i -> ImageByteResponse.builder().bytes(minioService.getFile(i)).link(i).build())
                .toList();
    }
}
