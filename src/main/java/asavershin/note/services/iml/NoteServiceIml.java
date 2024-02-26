package asavershin.note.services.iml;

import asavershin.note.entities.NoteEntity;
import asavershin.note.exceptions.EntityNotFoundException;
import asavershin.note.exceptions.FileException;
import asavershin.note.repositories.ImageRepository;
import asavershin.note.repositories.NoteRepository;
import asavershin.note.services.MinioService;
import asavershin.note.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteServiceIml implements NoteService {
    private final NoteRepository noteRepository;
    private final MinioService minioService;
    private final ImageRepository imageRepository;

    @Override
    public NoteEntity createEntity(NoteEntity note){
        return noteRepository.insert(note);
    }
    @Override
    @Cacheable(value = "NoteService::getEntityById", key = "#noteId")
    public NoteEntity getEntityById(Long noteId){
        return noteRepository.findById(noteId).orElseThrow(()->new EntityNotFoundException("Note not found with id: "+noteId));
    }
    @Override
    @Transactional
    @CacheEvict(value = "NoteService::getEntityById", key = "#noteId")
    public void deleteEntityById(Long noteId){
        if(!noteRepository.existById(noteId)){
            throw new EntityNotFoundException("Note not found with id: "+noteId);
        }
        var links = imageRepository.findLinksByNoteId(noteId);
        noteRepository.deleteById(noteId);
        minioService.deleteFiles(links);

    }
    @Override
    @Transactional
    @CacheEvict(value = "NoteService::getEntityById", key = "#note.noteId")
    public NoteEntity updateEntity(NoteEntity note){
        if(!noteRepository.existById(note.getNoteId())){
            throw new EntityNotFoundException("Note not found with id: "+note.getNoteId());
        }
        return noteRepository.update(note);
    }

    @Override
    public List<NoteEntity> findAll(Long pageNumber, Long pageSize) {
        return noteRepository.findAll(pageNumber, pageSize);
    }
}
