package ru.practicum.shareIt.user.mapper;

import ru.practicum.shareIt.user.User;
import ru.practicum.shareIt.user.dto.UserDto;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
