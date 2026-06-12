package ru.practicum.shareIt.item.dto;

import java.time.LocalDateTime;

public record CommentResponseDto(Long id, String text, Long item, Long author,
                                 String authorName, LocalDateTime created) {
}
