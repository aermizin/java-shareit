package ru.practicum.shareIt.user.mapper;

import ru.practicum.shareIt.user.User;
import ru.practicum.shareIt.user.dto.UserDto;
import ru.practicum.shareIt.user.dto.UserRequestDto;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toUser(UserRequestDto user) {
        return User.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
