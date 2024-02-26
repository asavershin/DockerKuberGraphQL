package asavershin.note.services.iml;

import asavershin.note.dto.ImageByteResponse;
import asavershin.note.entities.ImageEntity;
import asavershin.note.exceptions.EntityNotFoundException;
import asavershin.note.repositories.ImageRepository;
import asavershin.note.repositories.NoteRepository;
import asavershin.note.services.ImageService;
import asavershin.note.services.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ImageServiceIml implements ImageService {
    private final MinioService minioService;
    private final ImageRepository imageRepository;
    private final NoteRepository noteRepository;
    private final CacheManager cacheManager;

    @Transactional
    @Override
    @CacheEvict(value = "NoteService::getEntityById", key = "#noteId")
    public void uploadFile(Long noteId, MultipartFile image){
        if (!noteRepository.existById(noteId)) {
            throw new EntityNotFoundException("Note not found with id: " + noteId);
        }
        var imageEntity = new ImageEntity(
                null,
                image.getOriginalFilename(),
                image.getSize(),
                minioService.saveFile(image),
                noteId);
        try {
            imageRepository.insert(imageEntity);
        } catch (Exception ex) {
            minioService.deleteFiles(Collections.singletonList(imageEntity.getImageLink()));
            throw ex;
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id){
        if (!imageRepository.existById(id)) {
            throw new EntityNotFoundException("Image not found with id: " + id);
        }
        var deletedImage = imageRepository.deleteById(id);
        minioService.deleteFiles(Collections.singletonList(deletedImage.getImageLink()));
        cacheManager.getCache("NoteService::getEntityById").evict(deletedImage.getNoteId());
    }

    @Override
    @Transactional
    public List<ImageByteResponse> downloadImages(List<String> images){
        return images.stream()
                .map(i -> ImageByteResponse.builder().bytes(minioService.getFile(i)).link(i).build())
                .toList();
    }
}
