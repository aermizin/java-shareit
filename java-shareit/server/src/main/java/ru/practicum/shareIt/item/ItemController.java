package ru.practicum.shareIt.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareIt.item.dto.*;
import ru.practicum.shareIt.item.model.Comment;
import ru.practicum.shareIt.item.service.ItemService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    public Collection<ItemOwnerResponseDto> getItems(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.findAll(ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemOwnerResponseDto findItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
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
    public ItemResponseDto createItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                              @Valid @RequestBody ItemRequestDto newItem) {
        return itemService.create(ownerId, newItem);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createComment(@RequestHeader("X-Sharer-User-Id") Long authorId,
                                            @PathVariable Long itemId,
                                            @RequestBody CommentRequestDto commentRequest) {
        return itemService.createComment(authorId, itemId, commentRequest);
    }

    @PatchMapping("/{id}")
    public ItemResponseDto updatedItem(@PathVariable Long id,
                               @RequestHeader("X-Sharer-User-Id") Long ownerId,
                               @RequestBody ItemRequestDto updatedItem) {
        return itemService.updated(id, ownerId, updatedItem);
    }
}