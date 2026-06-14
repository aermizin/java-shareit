package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserRequestDto;

import java.util.Collection;

public interface UserService {
    Collection<UserResponseDto> findAll();

    UserResponseDto findUser(Long userId);

    UserResponseDto create(UserRequestDto newUser);

    UserResponseDto updated(Long userId, UserRequestDto updatedUser);

    void deleteUser(Long userId);
}
