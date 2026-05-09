package ru.practicum.shareIt.item.dao;

import ru.practicum.shareIt.item.model.Item;

import java.util.Collection;

public interface ItemDao {
    Collection<Item> getAllUserItems(Long ownerId);

    Item getItem(Long id);

    Item createItem(Item newItem);

    Item updatedItem(Long itemId, Item updatedItem);

    Collection<Item> searchItemsByText(String text);
}
