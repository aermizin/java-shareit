package ru.practicum.shareIt.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareIt.item.dto.ItemDto;
import ru.practicum.shareIt.item.dto.ItemRequestDto;
import ru.practicum.shareIt.item.service.ItemService;

import java.util.Collection;
import java.util.Collections;

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
    public Collection<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.findAll(ownerId);
    }

    @GetMapping("/{id}")
    public ItemDto findItem(@PathVariable Long id) {
        return itemService.findItem(id);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestParam String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        return itemService.searchItems(text);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                              @Valid @RequestBody ItemRequestDto newItem) {
        return itemService.create(ownerId, newItem);
    }

    @PatchMapping("/{id}")
    public ItemDto updatedItem(@PathVariable Long id,
                               @RequestHeader("X-Sharer-User-Id") Long ownerId,
                               @RequestBody ItemRequestDto updatedItem) {
        return itemService.updated(id, ownerId, updatedItem);
    }
}