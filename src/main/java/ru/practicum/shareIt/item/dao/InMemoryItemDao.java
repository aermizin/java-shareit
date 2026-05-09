package ru.practicum.shareIt.item.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareIt.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InMemoryItemDao implements ItemDao {
    Map<Long, Item> items = new HashMap<>();

    private long nextItemId = 1;

    @Override
    public Collection<Item> getAllUserItems(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public Item getItem(Long id) {
        return items.get(id);
    }

    @Override
    public Item createItem(Item newItem) {
        newItem.setId(nextItemId++);
        items.put(newItem.getId(), newItem);
        return newItem;
    }

    @Override
    public Item updatedItem(Long itemId, Item item) {
        Item updatedItem = items.get(itemId);

        if (item.getName() != null) {
            updatedItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updatedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }

        return updatedItem;
    }

    @Override
    public Collection<Item> searchItemsByText(String text) {
        String searchTerm = text.toLowerCase().trim();

        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item ->
                    (item.getName().toLowerCase().contains(searchTerm)) ||
                    (item.getDescription().toLowerCase().contains(searchTerm)))
                .collect(Collectors.toList());
    }
}
