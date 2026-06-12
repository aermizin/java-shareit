package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestDto {
    @NotBlank(message = "Описание запроса не может быть null или пустым.")
    private String description;
}
