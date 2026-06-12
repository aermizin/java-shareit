package ru.practicum.shareIt.item.service;

import ru.practicum.shareIt.item.dto.*;

import java.util.List;

public interface ItemService {

    List<ItemFullResponseDto> findAll(Long ownerId);

    ItemFullResponseDto findItemById(Long userId, Long itemId);

    List<ItemResponseDto> searchItems(String text);

    ItemResponseDto create(Long ownerId, ItemRequestDto newItem);

    CommentResponseDto createComment(Long authorId, Long itemId, CommentRequestDto commentRequest);

    ItemResponseDto updated(Long id, Long ownerId, ItemRequestDto updatedItem);
}
