package asavershin.note.repositories.iml;

import asavershin.generated.package_.tables.Image;
import asavershin.note.entities.ImageEntity;
import asavershin.note.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class ImageRepositoryIml implements ImageRepository {
    private final DSLContext dslContext;
    private final Image image = Image.IMAGE;
    @Override
    public List<String> deleteAllByNoteId(Long noteId) {
        return dslContext.deleteFrom(image)
                .where(image.NOTE_ID.eq(noteId))
                .returning(image.IMAGE_LINK)
                .fetch()
                .map(r -> r.get(image.IMAGE_LINK));
    }

    @Override
    public void insert(ImageEntity imageEntity) {
        dslContext.insertInto(image)
                .set(dslContext.newRecord(image, imageEntity))
                .execute();
    }

    @Override
    public String deleteById(Long id) {
        return dslContext.deleteFrom(image)
                .where(image.IMAGE_ID.eq(id))
                .returning(image.IMAGE_LINK)
                .fetch()
                .map(r -> r.get(image.IMAGE_LINK))
                .get(0);
    }

    @Override
    public List<String> findLinksByNoteId(Long noteId){
        return dslContext.select(image.IMAGE_LINK).from(image)
                .where(image.NOTE_ID.eq(noteId))
                .fetchInto(String.class);
    }

    @Override
    public boolean existById(Long id) {
        return dslContext.fetchExists(
                dslContext.selectFrom(image)
                        .where(image.IMAGE_ID.eq(id))
        );
    }

//    @Override
//    public ImageEntity insert(ImageEntity imageEntity) {
//        return Objects.requireNonNull(dslContext.insertInto(image)
//                        .set(image.IMAGE_NAME, imageEntity.getImageName())
//                        .set(image.IMAGE_SIZE, imageEntity.getImageSize())
//                        .set(image.IMAGE_LINK, imageEntity.getImageLink())
//                        .set(image.NOTE_ID, imageEntity.getImageId())
//                        .returning()
//                        .fetchOne())
//                .into(ImageEntity.class);
//    }
//
//    @Override
//    public ImageEntity update(ImageEntity imageEntity){
//        return Objects.requireNonNull(dslContext.update(image)
//                        .set(image.IMAGE_NAME, imageEntity.getImageName())
//                        .where(image.NOTE_ID.eq(imageEntity.getNoteId()))
//                        .returning()
//                        .fetchOne())
//                .into(ImageEntity.class);
//    }

        //    @Override
        //    public Optional<ImageEntity> findById(Long id){
        //        return dslContext.selectFrom(image)
        //                .where(i.NOTE_ID.eq(Math.toIntExact(id)))
        //                .fetchOptionalInto(NoteEntity.class);
        //    }
        //
        //    @Override
        //    public Boolean delete(Long id) {
        //        return null;
        //    }
}
