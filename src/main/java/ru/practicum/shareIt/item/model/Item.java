package ru.practicum.shareIt.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareIt.request.ItemRequest;

@Data
@Builder
public class Item {
    private Long id;

    @NotBlank(message = "Название товара не может быть null или пустым.")
    private String name;

    @NotBlank(message = "Описание товара не может быть null или пустым.")
    private String description;

    @NotNull(message = "Статус товара не может быть null.")
    private Boolean available;

    private Long owner;

    private ItemRequest request;
}
