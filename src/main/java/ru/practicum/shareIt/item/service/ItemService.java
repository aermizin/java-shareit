package ru.practicum.shareIt.item.service;

import ru.practicum.shareIt.item.dto.ItemDto;
import ru.practicum.shareIt.item.model.Item;

import java.util.Collection;

public interface ItemService {

    Collection<ItemDto> findAll(Long ownerId);

    Collection<ItemDto> searchItems(String text);

    ItemDto findItem(Long id);

    ItemDto create(Long ownerId, Item newItem);

    ItemDto updated(Long id, Long ownerId, Item updatedItem);
}
