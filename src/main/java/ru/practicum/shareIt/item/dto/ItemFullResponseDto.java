package ru.practicum.shareIt.item.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ItemOwnerResponseDto(Long id, String name, String description,
                                   Boolean available, LocalDateTime lastBooking, LocalDateTime nextBooking,
                                   List<CommentResponseDto> comments, Long request) {
}
