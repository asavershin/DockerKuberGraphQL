package asavershin.note.repositories.iml;

import asavershin.generated.package_.tables.Users;
import asavershin.note.entities.NoteEntity;
import asavershin.note.entities.UserEntity;
import asavershin.note.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryIml implements UserRepository {
    private final DSLContext dslContext;
    private final Users user = Users.USERS;

    @Override
    public UserEntity save(UserEntity userEntity) {
        return dslContext.insertInto(user)
                .set(dslContext.newRecord(user, userEntity))
                .returning()
                .fetchInto(UserEntity.class).get(0);
    }

    @Override
    public Optional<UserEntity> findByUserEmail(String email) {
        return Optional.ofNullable(
                dslContext.selectFrom(user)
                        .where(user.USER_EMAIL.eq(email))
                        .fetchInto(UserEntity.class).get(0)
        );
    }

    @Override
    public boolean existByUserEmail(String userEmail) {
        return dslContext.fetchExists(
                dslContext.selectFrom(user)
                        .where(user.USER_EMAIL.eq(userEmail))
        );
    }

    @Override
    public Optional<UserEntity> findByUserId(Long userId) {
        return Optional.ofNullable(
                dslContext.selectFrom(user)
                        .where(user.USER_ID.eq(userId))
                        .fetchInto(UserEntity.class).get(0)
        );
    }
}
