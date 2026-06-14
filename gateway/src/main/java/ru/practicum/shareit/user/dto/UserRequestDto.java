package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    @NotBlank(message = "Имя пользователя не может быть null или пустым.")
    private String name;

    @NotBlank(message = "Email пользователя не может быть null или пустым.")
    @Email(message = "Email пользователя должен быть заполнен корректно.")
    private String email;
}
