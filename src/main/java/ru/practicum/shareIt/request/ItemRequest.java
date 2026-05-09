package ru.practicum.shareIt.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareIt.user.User;

import java.time.LocalDateTime;

@Data
@Builder
public class ItemRequest {
    private Long id;

    private String description;

    private User requestor;

    private LocalDateTime created;
}
