package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;


public record RequestResponseDto(Long id, String description, User requestor,
                                 LocalDateTime created) {
}

