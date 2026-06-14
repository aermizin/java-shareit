package ru.practicum.shareit.item.dto;

import java.time.Instant;
import java.util.List;

public record ItemFullResponseDto(Long id, String name, String description,
                                  Boolean available, Instant lastBooking, Instant nextBooking,
                                  List<CommentResponseDto> comments, Long request) {
}

