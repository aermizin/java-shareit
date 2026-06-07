package ru.practicum.shareIt.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestDto {
        @NotBlank(message = "Название товара не может быть null или пустым.")
        private String name;

        @NotBlank(message = "Описание товара не может быть null или пустым.")
        private String description;

        @NotNull(message = "Статус товара не может быть null.")
        private Boolean available;

        private Long request;
}
