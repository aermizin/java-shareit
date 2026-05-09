package ru.practicum.shareIt.user.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareIt.exception.NotFoundException;
import ru.practicum.shareIt.user.User;
import ru.practicum.shareIt.user.dao.UserDao;
import ru.practicum.shareIt.user.dto.UserDto;
import ru.practicum.shareIt.user.mapper.UserMapper;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    public Collection<UserDto> findAll() {
        return userDao.getAllUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findUser(Long id) {
        User user = userDao.getUser(id);
        if (user == null) {
            log.warn("Пользователь с id = {} не найден", id);
            throw new NotFoundException("Пользователь не был найден");
        }
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto create(User user) {
        validationUser(user);
        User newUser = userDao.createUser(user);
        return UserMapper.toUserDto(newUser);
    }

    @Override
    public UserDto updated(Long userId, User user) {
        findUser(userId);
        user.setId(userId);

        if (user.getEmail() != null) {
            validationUser(user);
        }

        User updatedUser = userDao.updatedUser(user);
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        userDao.deleteUser(id);
    }

    public void validationUser(User user) {
        if (userDao.checkEmail(user)) {
            log.warn("Пользователь c таким email = {} уже существует", user.getEmail());
            throw new ValidationException("Пользователь с таким email уже существует");
        }
    }
}
