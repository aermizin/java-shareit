package ru.practicum.shareIt.user.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareIt.exception.NotFoundException;
import ru.practicum.shareIt.item.mapper.ItemMapper;
import ru.practicum.shareIt.user.User;
import ru.practicum.shareIt.user.dao.UserDao;
import ru.practicum.shareIt.user.dto.UserDto;
import ru.practicum.shareIt.user.dto.UserRequestDto;
import ru.practicum.shareIt.user.mapper.UserMapper;

import java.util.Collection;
import java.util.Optional;
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
        return userDao.getUser(id)
                .map(UserMapper::toUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не был найден"));
    }

    @Override
    public UserDto create(UserRequestDto userRequest) {
        User user = UserMapper.toUser(userRequest);
        validationUser(user);
        User newUser = userDao.createUser(user);
        return UserMapper.toUserDto(newUser);
    }

    @Override
    public UserDto updated(Long userId, UserRequestDto userRequest) {
        findUser(userId);
        User user = UserMapper.toUser(userRequest);
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

    private void validationUser(User user) {
        if (userDao.checkEmail(user)) {
            log.warn("Пользователь c таким email = {} уже существует", user.getEmail());
            throw new ValidationException("Пользователь с таким email уже существует");
        }
    }
}
