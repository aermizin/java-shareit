package ru.practicum.shareIt.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private Long id;

    @NotBlank(message = "Имя пользователя не может быть null или пустым.")
    private String name;

    @Email(message = "Email пользователя должен быть заполнен корректно.")
    @NotBlank(message = "Email пользователя не может быть null или пустым.")
    private String email;
}
