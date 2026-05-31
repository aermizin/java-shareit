package ru.practicum.shareIt.item.mapper;

import ru.practicum.shareIt.item.dto.ItemDto;
import ru.practicum.shareIt.item.dto.ItemRequestDto;
import ru.practicum.shareIt.item.model.Item;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest() != null ? item.getRequest().getId() : null)
                .build();
    }

    public static Item toItem(Long ownerId, ItemRequestDto itemRequest) {
        return Item.builder()
                .name(itemRequest.getName())
                .description(itemRequest.getDescription())
                .available(itemRequest.getAvailable())
                .owner(ownerId)
                .build();
    }
}
