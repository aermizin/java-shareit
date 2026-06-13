package ru.practicum.shareIt.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

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
    public Collection<ItemFullResponseDto> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.findAll(userId);
    }

    @GetMapping("/{itemId}")
    public ItemFullResponseDto findItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @PathVariable Long itemId) {
        return itemService.findItemById(userId, itemId);
    }

    @GetMapping("/search")
    public Collection<ItemResponseDto> searchItems(@RequestParam String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        return itemService.searchItems(text);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponseDto createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @RequestBody ItemRequestDto newItem) {
        return itemService.create(userId, newItem);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @PathVariable Long itemId,
                                            @RequestBody CommentRequestDto commentRequest) {
        return itemService.createComment(userId, itemId, commentRequest);
    }

    @PatchMapping("/{id}")
    public ItemResponseDto updatedItem(@PathVariable Long id,
                                       @RequestHeader("X-Sharer-User-Id") long userId,
                                       @RequestBody ItemRequestDto updatedItem) {
        return itemService.updated(id, userId, updatedItem);
    }
}