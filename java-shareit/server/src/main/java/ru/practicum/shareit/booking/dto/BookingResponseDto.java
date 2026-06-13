package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.Instant;

public record BookingResponseDto(Long id,
                                 @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Novosibirsk") Instant start,
                                 @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Novosibirsk") Instant end,
                                 ItemResponseDto item,
                                 UserResponseDto booker,
                                 BookingStatus status) {
}
