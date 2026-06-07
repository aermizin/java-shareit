package ru.practicum.shareIt.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareIt.booking.model.Booking;
import ru.practicum.shareIt.booking.repository.BookingRepository;
import ru.practicum.shareIt.exception.NotFoundException;
import ru.practicum.shareIt.exception.ValidationException;
import ru.practicum.shareIt.item.dto.*;
import ru.practicum.shareIt.item.mapper.CommentMapper;
import ru.practicum.shareIt.item.mapper.ItemMapper;
import ru.practicum.shareIt.item.model.Comment;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.item.repository.CommentRepository;
import ru.practicum.shareIt.item.repository.ItemRepository;
import ru.practicum.shareIt.request.model.ItemRequest;
import ru.practicum.shareIt.request.repository.ItemRequestRepository;
import ru.practicum.shareIt.user.model.User;
import ru.practicum.shareIt.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public List<ItemFullResponseDto> findAll(Long ownerId) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным ID не найден"));

        List<Item> items = itemRepository.findItemsFullByOwnerId(ownerId);

        if (items.isEmpty()) {
            log.warn("Не найдено ни одной вещи у пользователя с ID = {}", ownerId);
            return List.of();
        }

        List<Long> itemIds = items.stream().map(Item::getId).toList();
        LocalDateTime now = LocalDateTime.now();

        Map<Long, Booking> lastBookingMap = bookingRepository.findLastBookingsForItems(itemIds, now).stream()
                .collect(Collectors.toMap(booking -> booking.getItem().getId(), Function.identity()));

        Map<Long, Booking> nextBookingMap = bookingRepository.findNextBookingsForItems(itemIds, now).stream()
                .collect(Collectors.toMap(booking -> booking.getItem().getId(), Function.identity()));

        return items.stream()
                .map(item -> {
                    Booking last = lastBookingMap.get(item.getId());
                    Booking next = nextBookingMap.get(item.getId());
                    return ItemMapper.toItemFullDto(item, last, next);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ItemFullResponseDto findItemById(Long userId, Long itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным ID не найден"));
        Item item = itemRepository.findItemFullById(itemId)
                        .orElseThrow(() -> new NotFoundException("Вещь с указанным ID не найдена"));

        List<Long> itemIds = List.of(itemId);
        LocalDateTime now = LocalDateTime.now();

        Booking last = null;
        Booking next = null;

        if (item.getOwner().getId().equals(userId)) {
            Map<Long, Booking> lastBookingMap = bookingRepository.findLastBookingsForItems(itemIds, now).stream()
                    .collect(Collectors.toMap(booking -> booking.getItem().getId(), Function.identity()));

            Map<Long, Booking> nextBookingMap = bookingRepository.findNextBookingsForItems(itemIds, now).stream()
                    .collect(Collectors.toMap(booking -> booking.getItem().getId(), Function.identity()));

            last = lastBookingMap.get(itemId);
            next = nextBookingMap.get(itemId);
        }

        return ItemMapper.toItemFullDto(item, last, next);
    }

    @Override
    public List<ItemResponseDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            log.warn("Поисковый запрос не может быть пустым");
            return List.of();
        }

        return itemRepository.searchItemsByText(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemResponseDto create(Long ownerId, ItemRequestDto itemRequest) {
        Item item = initializeItem(ownerId, itemRequest);
        Item saved = itemRepository.save(item);
        log.info("Создана новая вещь с ID = {}", saved.getId());
        return ItemMapper.toItemDto(saved);
    }

    @Override
    @Transactional
    public CommentResponseDto createComment(Long authorId, Long itemId, CommentRequestDto commentRequest) {
        if (!bookingRepository.isUserBookedItem(authorId, itemId, LocalDateTime.now())) {
            log.warn("Пользователь с ID = {} попытался оставить комментарий к вещи с ID = {}, но не имеет " +
                            "завершённого бронирования", authorId, itemId);
            throw new ValidationException("Пользователь не может оставить комментарий: нет завершённого " +
                    "бронирования для этой вещи");
        }

        Comment comment = initializeComment(authorId, itemId, commentRequest);
        Comment saved = commentRepository.save(comment);
        log.info("Создан новый комментарий с ID = {}", saved.getId());
        return CommentMapper.toCommentDto(saved);
    }

    @Override
    @Transactional
    public ItemResponseDto updated(Long itemId, Long ownerId, ItemRequestDto itemRequest) {
        validationUpdatedItem(itemId, ownerId);
        Item updated = initializeItem(ownerId, itemRequest);

        if (itemRequest.getName() != null) {
            updated.setName(itemRequest.getName());
        }

        if (itemRequest.getDescription() != null) {
            updated.setDescription(itemRequest.getDescription());
        }

        log.info("Обновлена вещь с ID = {}", itemId);
        return ItemMapper.toItemDto(updated);
    }

    private void validationUpdatedItem(Long itemId, Long ownerId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с указанным ID не найдена"));

        if (!ownerId.equals(item.getOwner().getId())) {
            log.warn("Пользователь с ID = {} попытался обновить вещь с ID = {}, " +
                    "не являясь её владельцем", ownerId, itemId);
            throw new NotFoundException("Только владелец может обновить вещь");
        }
    }

    private Item initializeItem(Long ownerId, ItemRequestDto itemRequest) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new NotFoundException("Пользователь с указанным" +
                "ID не найден"));
        ItemRequest request = null;
        if (itemRequest.getRequest() != null) {
            request = itemRequestRepository.findById(itemRequest.getRequest())
                    .orElseThrow(() -> new NotFoundException("Запрос с указанным ID на создание не найден"));
        }

        return ItemMapper.toItem(owner, request, itemRequest);
    }

    private Comment initializeComment(Long authorId, Long itemId,  CommentRequestDto commentRequest) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с указанным ID не найдена"));
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным ID не найден"));
        String commentText = commentRequest.getText();

        return CommentMapper.toComment(item, author, commentText);
    }
}