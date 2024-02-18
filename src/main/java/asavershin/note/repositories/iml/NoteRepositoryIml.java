package asavershin.note.repositories.iml;

import asavershin.note.entities.NoteEntity;
import asavershin.note.repositories.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import asavershin.generated.package_.tables.Note;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NoteRepositoryIml implements NoteRepository {

    private final DSLContext dslContext;
    private final Note note = Note.NOTE;

    @Override
    public NoteEntity insert(NoteEntity noteEntity) {
        return Objects.requireNonNull(dslContext.insertInto(note)
                        .set(note.NOTE_HEADER, noteEntity.getNoteHeader())
                        .set(note.NOTE_MESSAGE, noteEntity.getNoteMessage())
                        .returning()
                        .fetchOne())
                .into(NoteEntity.class);
    }

    @Override
    public NoteEntity update(NoteEntity noteEntity) {
         return Objects.requireNonNull(dslContext.update(note)
                         .set(note.NOTE_HEADER, noteEntity.getNoteHeader())
                         .set(note.NOTE_MESSAGE, noteEntity.getNoteMessage())
                         .where(note.NOTE_ID.eq(Math.toIntExact(noteEntity.getNoteId())))
                         .returning()
                         .fetchOne())
                .into(NoteEntity.class);
    }

    @Override
    public Optional<NoteEntity> findById(Long id) {
        return dslContext.selectFrom(note)
                .where(note.NOTE_ID.eq(Math.toIntExact(id)))
                .fetchOptionalInto(NoteEntity.class);
    }

    @Override
    public Boolean delete(Long id) {
        int deletedRows = dslContext.deleteFrom(note)
                .where(note.NOTE_ID.eq(Math.toIntExact(id)))
                .execute();

        return deletedRows > 0;
    }

    @Override
    public boolean existById(Long id) {
        return dslContext.fetchExists(
                dslContext.selectFrom(note)
                        .where(note.NOTE_ID.eq(Math.toIntExact(id)))
        );
    }

    @Override
    public List<NoteEntity> findAll(Long pageNumber, Long pageSize) {
        return dslContext.selectFrom(note)
                .orderBy(note.NOTE_ID)
                .offset(pageNumber * pageSize)
                .limit(pageSize)
                .fetchInto(NoteEntity.class);
    }

}

