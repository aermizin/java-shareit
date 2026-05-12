package ru.practicum.shareIt.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareIt.request.ItemRequest;

@Data
@Builder
public class Item {
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long owner;

    private ItemRequest request;
}
