package asavershin.note.services.iml;

import asavershin.note.entities.ImageEntity;
import asavershin.note.entities.NoteEntity;
import asavershin.note.entities.OperationEntity;
import asavershin.note.entities.OperationType;
import asavershin.note.exceptions.EntityNotFoundException;
import asavershin.note.exceptions.FileException;
import asavershin.note.repositories.ImageRepository;
import asavershin.note.repositories.NoteRepository;
import asavershin.note.services.MinioService;
import asavershin.note.services.NoteService;
import asavershin.note.services.OperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteServiceIml implements NoteService {
    private final NoteRepository noteRepository;
    private final MinioService minioService;
    private final ImageRepository imageRepository;
    private final OperationService operationService;

    @Override
    public NoteEntity createEntity(NoteEntity note){
        note = noteRepository.insert(note);
        operationService.logOperation(
                new OperationEntity(null,
                        String.format("Write note : %s", note),
                        LocalDateTime.now(),
                        OperationType.WRITE)

        );
        return note;
    }
    @Override
    @Cacheable(value = "NoteService::getEntityById", key = "#noteId")
    public NoteEntity getEntityById(Long noteId){
        var noteEntity =  noteRepository.findById(noteId).orElseThrow(()->new EntityNotFoundException("Note not found with id: "+noteId));
        operationService.logOperation(
                new OperationEntity(null,
                        String.format("Read note : %s", noteEntity),
                        LocalDateTime.now(),
                        OperationType.READ)
        );
        return noteEntity;
    }
    @Override
    @Transactional
    @CacheEvict(value = "NoteService::getEntityById", key = "#noteId")
    public void deleteEntityById(Long noteId){
        var note = noteRepository.findById(noteId);
        if(note.isEmpty()){
            throw new EntityNotFoundException("Note not found with id: "+noteId);
        }
        var deletedImages = imageRepository.deleteAllByNoteId(noteId);
        noteRepository.deleteById(noteId);
        minioService.deleteFiles(deletedImages.stream().map(ImageEntity::getImageLink).toList());

        operationService.logOperation(
                new OperationEntity(null,
                        String.format("Delete note : %s", note),
                        LocalDateTime.now(),
                        OperationType.DELETE)
        );
        operationService.logOperation(
                new OperationEntity(null,
                        String.format("Delete images : %s", deletedImages),
                        LocalDateTime.now(),
                        OperationType.DELETE)
        );
    }
    @Override
    @Transactional
    @CacheEvict(value = "NoteService::getEntityById", key = "#note.noteId")
    public NoteEntity updateEntity(NoteEntity note){
        if(!noteRepository.existById(note.getNoteId())){
            throw new EntityNotFoundException("Note not found with id: "+note.getNoteId());
        }
        note = noteRepository.update(note);
        operationService.logOperation(
                new OperationEntity(null,
                        String.format("Delete note : %s", note),
                        LocalDateTime.now(),
                        OperationType.UPDATE)

        );
        return note;
    }

    @Override
    public List<NoteEntity> findAll(Long pageNumber, Long pageSize) {
        var notes = noteRepository.findAll(pageNumber, pageSize);
        operationService.logOperation(
                new OperationEntity(null,
                        String.format("Read notes : %s", notes),
                        LocalDateTime.now(),
                        OperationType.READ)

        );
        return notes;
    }
}
