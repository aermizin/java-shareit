package ru.practicum.shareIt.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareIt.item.dto.ItemDto;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.item.service.ItemService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.findAll(ownerId);
    }

    @GetMapping("/{id}")
    public ItemDto findItem(@PathVariable Long id) {
        return itemService.findItem(id);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                              @Valid @RequestBody Item newItem) {
        return itemService.create(ownerId, newItem);
    }

    @PatchMapping("/{id}")
    public ItemDto updatedItem(@PathVariable Long id,
                               @RequestHeader("X-Sharer-User-Id") Long ownerId,
                               @RequestBody Item updatedItem) {
        return itemService.updated(id, ownerId, updatedItem);
    }
}