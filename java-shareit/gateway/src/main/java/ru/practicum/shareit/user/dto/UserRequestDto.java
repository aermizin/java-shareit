package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequestDto {
    @NotBlank(message = "Имя пользователя не может быть null или пустым.")
    private String name;

    @Email(message = "Email пользователя должен быть заполнен корректно.")
    @NotBlank(message = "Email пользователя не может быть null или пустым.")
    private String email;
}
