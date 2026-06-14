package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Instant;
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
    private final RequestRepository itemRequestRepository;

    @Override
    public List<ItemFullResponseDto> findAll(long ownerId) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным ID не найден"));

        List<Item> items = itemRepository.findItemsFullByOwnerId(ownerId);

        if (items.isEmpty()) {
            log.warn("Не найдено ни одной вещи у пользователя с ID = {}", ownerId);
            return List.of();
        }

        List<Long> itemIds = items.stream().map(Item::getId).toList();
        Instant now = Instant.now();

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
    public ItemFullResponseDto findItemById(long userId, Long itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным ID не найден"));
        Item item = itemRepository.findItemFullById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с указанным ID не найдена"));

        List<Long> itemIds = List.of(itemId);
        Instant now = Instant.now();

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
    public ItemResponseDto create(long userId, ItemRequestDto itemRequest) {
        Item item = initializeItem(userId, itemRequest);
        Item saved = itemRepository.save(item);
        log.info("Создана новая вещь с ID = {}", saved.getId());
        return ItemMapper.toItemDto(saved);
    }

    @Override
    @Transactional
    public CommentResponseDto createComment(long userId, Long itemId, CommentRequestDto commentRequest) {
        if (!bookingRepository.isUserBookedItem(userId, itemId, Instant.now())) {
            log.warn("Пользователь с ID = {} попытался оставить комментарий к вещи с ID = {}, но не имеет " +
                    "завершённого бронирования", userId, itemId);
            throw new ValidationException("Пользователь не может оставить комментарий: нет завершённого " +
                    "бронирования для этой вещи");
        }

        Comment comment = initializeComment(userId, itemId, commentRequest);
        Comment saved = commentRepository.save(comment);
        log.info("Создан новый комментарий с ID = {}", saved.getId());
        return CommentMapper.toCommentDto(saved);
    }

    @Override
    @Transactional
    public ItemResponseDto updated(long userId, Long itemId, ItemRequestDto itemRequest) {
        validationUpdatedItem(itemId, userId);
        Item updated = initializeItem(userId, itemRequest);

        if (itemRequest.getName() != null) {
            updated.setName(itemRequest.getName());
        }

        if (itemRequest.getDescription() != null) {
            updated.setDescription(itemRequest.getDescription());
        }

        log.info("Обновлена вещь с ID = {}", itemId);
        return ItemMapper.toItemDto(updated);
    }

    private void validationUpdatedItem(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с указанным ID не найдена"));

        if (!userId.equals(item.getOwner().getId())) {
            log.warn("Пользователь с ID = {} попытался обновить вещь с ID = {}, " +
                    "не являясь её владельцем", userId, itemId);
            throw new NotFoundException("Только владелец может обновить вещь");
        }
    }

    private Item initializeItem(long userId, ItemRequestDto itemRequest) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с указанным" +
                "ID не найден"));
        Request request = null;
        if (itemRequest.getRequestId() != null) {
            request = itemRequestRepository.findById(itemRequest.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Запрос с указанным ID на создание не найден"));
        }

        return ItemMapper.toItem(owner, request, itemRequest);
    }

    private Comment initializeComment(long userId, Long itemId,  CommentRequestDto commentRequest) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с указанным ID не найдена"));
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным ID не найден"));
        String commentText = commentRequest.getText();

        return CommentMapper.toComment(item, author, commentText);
    }
}