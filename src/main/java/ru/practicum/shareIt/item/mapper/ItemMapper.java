package ru.practicum.shareIt.item.mapper;

import ru.practicum.shareIt.booking.model.Booking;
import ru.practicum.shareIt.item.dto.ItemFullResponseDto;
import ru.practicum.shareIt.item.dto.ItemRequestDto;
import ru.practicum.shareIt.item.dto.ItemResponseDto;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.request.model.ItemRequest;
import ru.practicum.shareIt.user.model.User;

import java.util.stream.Collectors;

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

    public static ItemFullResponseDto toItemFullDto(Item item, Booking lastBooking, Booking nextBooking) {
        return new ItemFullResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking != null ? lastBooking.getEnd() : null,
                nextBooking != null ? nextBooking.getStart() : null,
                item.getComments().stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }
}
