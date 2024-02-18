package asavershin.note.services;

import asavershin.note.entities.NoteEntity;

import java.util.List;

public interface NoteService extends Service<NoteEntity, Long> {
    List<NoteEntity> findAll(Long pageNumber, Long pageSize);
}
