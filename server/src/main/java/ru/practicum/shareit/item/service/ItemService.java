package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {

    List<ItemFullResponseDto> findAll(long userId);

    ItemFullResponseDto findItemById(long userId, Long itemId);

    List<ItemResponseDto> searchItems(String text);

    ItemResponseDto create(long userId, ItemRequestDto newItem);

    CommentResponseDto createComment(long userId, Long itemId, CommentRequestDto commentRequest);

    ItemResponseDto updated(long userIdId, Long itemId, ItemRequestDto updatedItem);
}

