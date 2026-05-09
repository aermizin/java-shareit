package ru.practicum.shareIt.item.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareIt.exception.NotFoundException;
import ru.practicum.shareIt.item.ItemMapper;
import ru.practicum.shareIt.item.dao.ItemDao;
import ru.practicum.shareIt.item.dto.ItemDto;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.user.service.UserService;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemDao itemDao;

    @Override
    public Collection<ItemDto> findAll(Long ownerId) {
        return itemDao.getAllUserItems(ownerId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            log.warn("Поисковый запрос пуст или равен null");
            return Collections.emptyList();
        }

        return itemDao.searchItemsByText(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto findItem(Long id) {
        Item item = itemDao.getItem(id);
        if (item == null) {
            log.warn("Товар с id = {} не найден", id);
            throw new NotFoundException("Товар не был найден");
        }

        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto create(Long ownerId, Item itemDto) {
        Item item = initializeItem(ownerId, itemDto);
        Item newItem = itemDao.createItem(item);

        return ItemMapper.toItemDto(newItem);
    }

    @Override
    public ItemDto updated(Long itemId, Long ownerId, Item itemDto) {
        Item item = initializeItem(ownerId, itemDto);
        validationUpdatedItem(itemId, ownerId);

        Item updatedItem = itemDao.updatedItem(itemId, item);

        return ItemMapper.toItemDto(updatedItem);
    }

    public void validationUpdatedItem(Long itemId, Long ownerId) {
        Item item = itemDao.getItem(itemId);

        if (!ownerId.equals(item.getOwner())) {
            log.warn("Пользователь с id = {} хотел изменить товар с id = {} ", ownerId, itemId);
            throw new ValidationException("Изменить товар может только его владелец");
        }
    }

    private Item initializeItem(Long ownerId, Item item) {
        userService.findUser(ownerId);
        item.setOwner(ownerId);

        return item;
    }
}
