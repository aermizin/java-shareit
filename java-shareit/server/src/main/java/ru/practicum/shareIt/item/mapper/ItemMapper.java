package ru.practicum.shareIt.item.mapper;

import ru.practicum.shareIt.item.dto.ItemRequestDto;
import ru.practicum.shareIt.item.dto.ItemResponseDto;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.request.model.ItemRequest;
import ru.practicum.shareIt.user.model.User;

public class ItemMapper {
    public static ItemResponseDto toItemDto(Item item) {
        return new ItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static Item toItem(User owner, ItemRequest request, ItemRequestDto itemRequest) {
        return Item.builder()
                .name(itemRequest.getName())
                .description(itemRequest.getDescription())
                .available(itemRequest.getAvailable())
                .owner(owner)
                .request(request)
                .build();
    }
}
