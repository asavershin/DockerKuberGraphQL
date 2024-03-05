package asavershin.note.services.iml;

import asavershin.note.entities.ImageEntity;
import asavershin.note.entities.NoteEntity;
import asavershin.note.entities.OperationEntity;
import asavershin.note.entities.OperationType;
import asavershin.note.exceptions.AuthException;
import asavershin.note.exceptions.EntityNotFoundException;
import asavershin.note.repositories.ImageRepository;
import asavershin.note.repositories.NoteRepository;
import asavershin.note.repositories.UserRepository;
import asavershin.note.services.MinioService;
import asavershin.note.services.NoteService;
import asavershin.note.services.OperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteServiceIml implements NoteService {
    private final NoteRepository noteRepository;
    private final MinioService minioService;
    private final ImageRepository imageRepository;
    private final OperationService operationService;
    private final UserRepository userRepository;

    @Override
    public NoteEntity createEntity(NoteEntity note, Long userId){
        log.atInfo().log("START CREATE NOTE");
        log.atInfo().log("GIVEN USERID: " + userId);
        note.setUserId(userId);
        note = noteRepository.save(note);
        operationService.logOperation(
                new OperationEntity(null,
                        String.format("Write note : %s", note),
                        LocalDateTime.now(),
                        OperationType.WRITE)

        );
        log.atInfo().log("END CREATE NOTE");
        return note;
    }
    @Override
    @Cacheable(value = "NoteServiceIml::getEntityById", key = "#noteId")
    public NoteEntity getEntityById(Long noteId){
        var noteEntity =  noteRepository.findByIdWithImages(noteId).orElseThrow(()->new EntityNotFoundException("Note not found with id: "+noteId));
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
    @CacheEvict(value = "NoteServiceIml::getEntityById", key = "#noteId")
    public void deleteEntityByUser(Long noteId, Long userId) {
        log.atInfo().log("START DELETE BY USER");
        log.atInfo().log("USER_ID: " + userId);
        var note = noteRepository.findByIdWithImages(noteId).orElseThrow(() -> new EntityNotFoundException("Note not found with id: "+noteId));
        log.atInfo().log("NOTE: " + note);
        if(note.getUserId() == null || !note.getUserId().equals(userId)){
            throw new AuthException("You are trying to delete a resource that is not yours");
        }
        deleteEntityById(note);
        log.atInfo().log("END DELETE NOTE");
    }

    @Override
    @Transactional
    @CacheEvict(value = "NoteServiceIml::getEntityById", key = "#noteId")
    public void deleteEntityByAdmin(Long noteId){
        var note = noteRepository.findByIdWithImages(noteId).orElseThrow(() -> new EntityNotFoundException("Note not found with id: "+noteId));
        deleteEntityById(note);
    }

    @Transactional
    @CacheEvict(value = "NoteServiceIml::getEntityById", key = "#note.noteId")
    public void deleteEntityById(NoteEntity note){
        var deletedImages = imageRepository.deleteAllByNoteId(note.getNoteId());
        noteRepository.deleteById(note.getNoteId());
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
    @CacheEvict(value = "NoteServiceIml::getEntityById", key = "#note.noteId")
    public NoteEntity updateEntity(final NoteEntity note, Long userId){
        var currentNode = noteRepository.findById(note.getNoteId()).orElseThrow(() -> new EntityNotFoundException("Note not found with id: " + note.getNoteId()));
        if(!currentNode.getUserId().equals(userId)){
            throw new AuthException("You are trying to delete a resource that is not yours");
        }
        var newNote = noteRepository.update(note);
        operationService.logOperation(
                new OperationEntity(null,
                        String.format("Delete note : %s", note),
                        LocalDateTime.now(),
                        OperationType.UPDATE)

        );
        return newNote;
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
