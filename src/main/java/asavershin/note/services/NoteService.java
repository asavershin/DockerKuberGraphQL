package asavershin.note.services;

import asavershin.note.entities.NoteEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NoteService {
    NoteEntity createEntity(NoteEntity note, Long userId);

    NoteEntity getEntityById(Long noteId);

    @Transactional
    void deleteEntityByUser(Long noteId, Long userId);

    @Transactional
    void deleteEntityByAdmin(Long noteId);

    @Transactional
    NoteEntity updateEntity(NoteEntity note, Long userId);

    List<NoteEntity> findAll(Long pageNumber, Long pageSize);
}
