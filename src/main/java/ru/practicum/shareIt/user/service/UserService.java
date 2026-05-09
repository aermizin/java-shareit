package ru.practicum.shareIt.user.service;

import ru.practicum.shareIt.user.User;
import ru.practicum.shareIt.user.dto.UserDto;

import java.util.Collection;

public interface UserService {

    Collection<UserDto> findAll();

    UserDto findUser(Long id);

    UserDto create(User newUser);

    UserDto updated(Long id, User updatedUser);

    void deleteUser(Long id);
}
