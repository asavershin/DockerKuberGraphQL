package asavershin.note.services.iml;

import asavershin.note.entities.UserEntity;
import asavershin.note.exceptions.EntityNotFoundException;
import asavershin.note.repositories.UserRepository;
import asavershin.note.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceIml implements UserService {

    private final UserRepository userRepository;

    @Override
    @Cacheable(value = "UserServiceIml::findByEmail", key = "#email")
    public UserEntity findByEmail(String email) {
        return userRepository.findByUserEmail(email).orElseThrow(() -> new EntityNotFoundException(
                "User not found with email: " + email
        ));
    }

}
