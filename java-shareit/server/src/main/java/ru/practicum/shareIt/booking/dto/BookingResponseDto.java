package ru.practicum.shareIt.booking.dto;

import ru.practicum.shareIt.booking.model.BookingStatus;
import ru.practicum.shareIt.item.dto.ItemResponseDto;
import ru.practicum.shareIt.user.dto.UserResponseDto;

import java.time.LocalDateTime;

public record BookingResponseDto(Long id, LocalDateTime start, LocalDateTime end, ItemResponseDto item,
                                 UserResponseDto booker, BookingStatus status) {
}
