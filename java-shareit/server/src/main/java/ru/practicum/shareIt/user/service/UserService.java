package ru.practicum.shareIt.user.service;

import ru.practicum.shareIt.user.dto.UserResponseDto;
import ru.practicum.shareIt.user.dto.UserRequestDto;

import java.util.Collection;

public interface UserService {
    Collection<UserResponseDto> findAll();

    UserResponseDto findUser(Long id);

    UserResponseDto create(UserRequestDto newUser);

    UserResponseDto updated(Long id, UserRequestDto updatedUser);

    void deleteUser(Long id);
}
