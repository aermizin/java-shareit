package ru.practicum.shareIt.user.service;

import ru.practicum.shareIt.user.dto.UserDto;
import ru.practicum.shareIt.user.dto.UserRequestDto;

import java.util.Collection;

public interface UserService {

    Collection<UserDto> findAll();

    UserDto findUser(Long id);

    UserDto create(UserRequestDto newUser);

    UserDto updated(Long id, UserRequestDto updatedUser);

    void deleteUser(Long id);
}
