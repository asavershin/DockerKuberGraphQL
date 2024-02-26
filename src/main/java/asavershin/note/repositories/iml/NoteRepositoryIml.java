package asavershin.note.repositories.iml;

import asavershin.generated.package_.tables.Image;
import asavershin.note.entities.ImageEntity;
import asavershin.note.entities.NoteEntity;
import asavershin.note.repositories.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.simpleflatmapper.jdbc.JdbcMapper;
import org.simpleflatmapper.jdbc.JdbcMapperFactory;
import org.springframework.stereotype.Repository;
import asavershin.generated.package_.tables.Note;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class NoteRepositoryIml implements NoteRepository {

    private final DSLContext dslContext;
    private final Note note = Note.NOTE;
    private final Image image = Image.IMAGE;
    JdbcMapper<NoteEntity> noteJdbcMapper =
            JdbcMapperFactory.newInstance().addKeys("noteId").newMapper(NoteEntity.class);

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
                        .where(note.NOTE_ID.eq(noteEntity.getNoteId()))
                        .returning()
                        .fetchOne())
                .into(NoteEntity.class);
    }

    @Override
    public Optional<NoteEntity> findById(Long id) {
        var rs = dslContext.select(note.fields())
                .select(
                        image.IMAGE_ID,
                        image.IMAGE_NAME,
                        image.IMAGE_SIZE,
                        image.IMAGE_LINK
                )
                .from(note)
                .leftJoin(image).using(note.NOTE_ID)
                .where(note.NOTE_ID.eq(id));

        var records = rs.collect(Collectors.toList());
        List<ImageEntity> images = rs.fetchInto(ImageEntity.class);
        return Optional.ofNullable(NoteEntity.builder()
                .noteId(records.get(0).get(note.NOTE_ID))
                .noteHeader(records.get(0).get(note.NOTE_HEADER))
                .noteMessage(records.get(0).get(note.NOTE_MESSAGE))
                .noteCreatedAt(records.get(0).get(note.NOTE_CREATED_AT))
                .noteLastUpdatedAt(records.get(0).get(note.NOTE_LAST_UPDATED_AT))
                .images(images)
                .build());
        }

    @Override
    public Boolean deleteById(Long id) {
        int deletedRows = dslContext.deleteFrom(note)
                .where(note.NOTE_ID.eq(id))
                .execute();

        return deletedRows > 0;
    }

    @Override
    public boolean existById(Long id) {
        return dslContext.fetchExists(
                dslContext.selectFrom(note)
                        .where(note.NOTE_ID.eq(id))
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

