package ru.practicum.shareIt.item.dto;

public record ItemResponseDto(Long id, String name, String description,
                              Boolean available, Long request) {}


