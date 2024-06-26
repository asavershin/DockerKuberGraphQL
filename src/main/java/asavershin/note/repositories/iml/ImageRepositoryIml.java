package asavershin.note.repositories.iml;

import asavershin.generated.package_.tables.Image;
import asavershin.generated.package_.tables.NoteImage;
import asavershin.generated.package_.tables.records.ImageRecord;
import asavershin.generated.package_.tables.records.NoteImageRecord;
import asavershin.note.entities.ImageEntity;
import asavershin.note.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.*;
import org.jooq.Record;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.*;


@Repository
@RequiredArgsConstructor
public class ImageRepositoryIml implements ImageRepository {
    private final DSLContext dslContext;
    private final Image image = Image.IMAGE;
    private final NoteImage noteImage = NoteImage.NOTE_IMAGE;

    @Override
    public List<ImageEntity> deleteAllByNoteId(Long noteId) {
        var deletedImages = dslContext.deleteFrom(noteImage)
                .where(noteImage.NOTE_ID.eq(noteId))
                .returning(noteImage.IMAGE_ID)
                .fetch()
                .map(NoteImageRecord::getImageId);

        return dslContext.deleteFrom(image)
                .where(image.IMAGE_ID.in(deletedImages))
                .returning()
                .fetchInto(ImageEntity.class);
    }

    @Override
    public ImageEntity save(ImageEntity imageEntity) {
        var newImage = dslContext.insertInto(image)
                .set(dslContext.newRecord(image, imageEntity))
                .returning()
                .fetchInto(ImageEntity.class).get(0);

        dslContext.insertInto(noteImage)
                .set(noteImage.IMAGE_ID, newImage.getImageId())
                .set(noteImage.NOTE_ID, imageEntity.getNoteId())
                .execute();
        return newImage;
    }


    @Override
    public boolean deleteById(Long id) {
        int deletedRows = dslContext.deleteFrom(image)
                .where(image.IMAGE_ID.eq(id))
                .execute();

        return deletedRows > 0;
    }

    @Override
    public List<String> findLinksByNoteId(Long noteId) {
        return dslContext.select(image.IMAGE_LINK).from(noteImage)
                .join(image).using(image.IMAGE_ID)
                .where(noteImage.NOTE_ID.eq(noteId))
                .fetchInto(String.class);
    }

    @Override
    public boolean existById(Long id) {
        return dslContext.fetchExists(
                dslContext.selectFrom(image)
                        .where(image.IMAGE_ID.eq(id))
        );
    }

    @Override
    public Optional<ImageEntity> findById(Long id) {
        return Optional.ofNullable(
                dslContext.selectFrom(image)
                        .where(image.IMAGE_ID.eq(id))
                        .fetchInto(ImageEntity.class).get(0)
        );
    }
}
