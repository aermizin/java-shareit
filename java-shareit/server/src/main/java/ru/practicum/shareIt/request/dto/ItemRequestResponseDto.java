package ru.practicum.shareIt.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareIt.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder
public class ItemRequestResponseDto {
    private Long id;

    private String description;

    private User requestor;

    private LocalDateTime created;
}
