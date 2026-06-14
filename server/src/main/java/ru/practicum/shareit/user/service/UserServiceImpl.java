package ru.practicum.shareit.user.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Collection<UserResponseDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto findUser(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным ID не найден"));
    }

    @Override
    @Transactional
    public UserResponseDto create(UserRequestDto userRequest) {
        User user = UserMapper.toUser(userRequest);

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            log.warn("Пользователь с таким email = {} уже существует", userRequest.getEmail());
            throw new ConflictException("Пользователь с таким email уже существует");
        }

        User saved = userRepository.save(user);
        log.info("Создан новый пользователь с ID = {}", saved.getId());
        return UserMapper.toUserDto(saved);
    }

    @Override
    @Transactional
    public UserResponseDto updated(Long userId, UserRequestDto userRequest) {
        User existing = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным ID не найден"));

        if (userRequest.getName() != null) {
            existing.setName(userRequest.getName());
        }

        if (userRequest.getEmail() != null) {
            if (userRepository.existsByEmailAndIdNot(userRequest.getEmail(), userId)) {
                log.warn("Пользователь с email = {} уже существует", userRequest.getEmail());
                throw new ConflictException("Пользователь с таким email уже существует");
            }
            existing.setEmail(userRequest.getEmail());
        }

        return UserMapper.toUserDto(existing);
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Пользователь с ID = {} удален", userId);
        userRepository.deleteById(userId);
    }
}
