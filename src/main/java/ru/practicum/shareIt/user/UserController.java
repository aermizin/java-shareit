package ru.practicum.shareIt.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareIt.user.dto.UserDto;
import ru.practicum.shareIt.user.dto.UserRequestDto;
import ru.practicum.shareIt.user.service.UserService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<UserDto> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDto findUser(@PathVariable Long id) {
        return userService.findUser(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserRequestDto newUser) {
        return userService.create(newUser);
    }

    @PatchMapping("/{id}")
    public UserDto updatedUser(@PathVariable Long id,
                               @RequestBody UserRequestDto updatedUser) {
        return userService.updated(id, updatedUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
