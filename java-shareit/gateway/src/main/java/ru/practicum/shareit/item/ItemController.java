package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.util.Collections;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Getting items: requesterId={}", userId);
        return itemClient.getItems(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable @Positive Long itemId) {
        log.info("Getting item: requesterId={}, itemId={}", userId, itemId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItemsByText(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam @Positive  String text) {
        if (text == null || text.isBlank()) {
            log.info("The received request with the text ={} parameter is null or empty.", text);
            return ResponseEntity.ok(Collections.emptyList());
        }

        log.info("Searching items by text: requesterId={}, text={}", userId, text);
        return itemClient.findItemsByText(userId, text);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestBody @Valid ItemRequestDto newItem) {
        log.info("Creating item: requesterId={}, newItem={}", userId, newItem);
        return itemClient.createItem(userId, newItem);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @PathVariable @Positive Long itemId,
                                                @RequestBody @Valid CommentRequestDto commentRequest) {
        log.info("Creating comment: requesterId={}, itemId={},  newComment={}", userId, itemId, commentRequest);
        return itemClient.createComment(userId, itemId, commentRequest);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable @Positive Long itemId,
                                             @RequestBody ItemRequestDto updatedItem) {
        log.info("Updating item: requesterId={}, itemId={}, updateData={}", userId,  itemId, updatedItem);
        return itemClient.updateItem(userId, itemId, updatedItem);
    }
}
