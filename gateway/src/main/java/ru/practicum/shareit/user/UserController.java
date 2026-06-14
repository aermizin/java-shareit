package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable @Positive Long userId) {
        log.info("Getting user: requesterId={}, userId={}", userId);
        return userClient.getUser(userId);
    }


    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserRequestDto newUser) {
        log.info("Creating user: newUser={}", newUser);
        return userClient.createUser(newUser);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable @Positive Long userId,
                                             @RequestBody UserRequestDto updatedUser) {
        log.info("Updating user: userId={}, updateData={}", userId, updatedUser);
        return userClient.updateUser(userId, updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable @Positive Long userId) {
        log.info("Deleting user: userId={}", userId);
        return userClient.deleteUser(userId);
    }
}

