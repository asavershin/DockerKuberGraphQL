package asavershin.note.controllers;

import asavershin.note.dto.UserDTO;
import asavershin.note.mappers.UserMapper;
import asavershin.note.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/profile/{userEmail}")
    public UserDTO getUserProfile(@PathVariable String userEmail){
        return userMapper.toDto(userService.findByEmail(userEmail));
    }
}
