package ru.practicum.shareit.request.repository.projection;

import java.time.LocalDateTime;

public interface RequestShortProjection {
    Long getId();

    String getDescription();

    LocalDateTime getCreated();

    UserInfo getRequestor();

    interface UserInfo {
        Long getId();

        String getName();

        String getEmail();
    }
}

