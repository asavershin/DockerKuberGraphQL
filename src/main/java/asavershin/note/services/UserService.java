package asavershin.note.services;

import asavershin.note.entities.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {
    UserEntity findByEmail(String email);
}
