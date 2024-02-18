package asavershin.note.services.iml;

import asavershin.note.entities.NoteEntity;
import asavershin.note.exceptions.EntityNotFoundException;
import asavershin.note.repositories.NoteRepository;
import asavershin.note.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteServiceIml implements NoteService {
    private final NoteRepository noteRepository;

    @Override
    public NoteEntity createEntity(NoteEntity note){
        return noteRepository.insert(note);
    }
    @Override
    public NoteEntity getEntityById(Long autoserviceId) throws EntityNotFoundException {
        return noteRepository.findById(autoserviceId).orElseThrow(()->new EntityNotFoundException("Note not found with id: "+autoserviceId));
    }
    @Override
    @Transactional
    public void deleteEntityById(Long autoserviceId) throws EntityNotFoundException {
        if(!noteRepository.existById(autoserviceId)){
            throw new EntityNotFoundException("Note not found with id: "+autoserviceId);
        }
        noteRepository.delete(autoserviceId);
    }
    @Override
    @Transactional
    public NoteEntity updateEntity(NoteEntity authoservice) throws EntityNotFoundException {
        if(!noteRepository.existById(authoservice.getNoteId())){
            throw new EntityNotFoundException("Note not found with id: "+authoservice.getNoteId());
        }
        return noteRepository.update(authoservice);
    }

    @Override
    public List<NoteEntity> findAll(Long pageNumber, Long pageSize) {
        return noteRepository.findAll(pageNumber, pageSize);
    }
}
