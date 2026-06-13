package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    @NotBlank(message = "Название товара не может быть null или пустым.")
    private String name;

    @NotBlank(message = "Описание товара не может быть null или пустым.")
    private String description;

    @NotNull(message = "Статус товара не может быть null.")
    private Boolean available;

    private Long requestId;
}

