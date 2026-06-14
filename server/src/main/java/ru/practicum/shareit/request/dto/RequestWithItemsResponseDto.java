package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemShortResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public record RequestWithItemsResponseDto(Long id, String description,
                                          LocalDateTime created, List<ItemShortResponseDto> items) {
}
