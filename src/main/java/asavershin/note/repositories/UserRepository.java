package asavershin.note.repositories;

import asavershin.note.entities.UserEntity;

import java.util.Optional;

public interface UserRepository {
    UserEntity save(UserEntity user);

    Optional<UserEntity> findByUserEmail(String email);

    boolean existByUserEmail(String userEmail);

    Optional<UserEntity> findByUserId(Long userId);
}
