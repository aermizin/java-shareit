package ru.practicum.shareIt.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareIt.exception.NotFoundException;
import ru.practicum.shareIt.item.mapper.ItemMapper;
import ru.practicum.shareIt.item.dao.ItemDao;
import ru.practicum.shareIt.item.dto.ItemDto;
import ru.practicum.shareIt.item.dto.ItemRequestDto;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.user.service.UserService;

import java.util.Collection;
import java.util.Optional;
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
        return itemDao.searchItemsByText(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto findItem(Long id) {
        return itemDao.getItem(id)
                .map(ItemMapper::toItemDto)
                .orElseThrow(() -> new NotFoundException("Товар не был найден"));
    }

    @Override
    public ItemDto create(Long ownerId, ItemRequestDto itemRequest) {
        Item item = initializeItem(ownerId, itemRequest);
        Item newItem = itemDao.createItem(item);

        return ItemMapper.toItemDto(newItem);
    }

    @Override
    public ItemDto updated(Long itemId, Long ownerId, ItemRequestDto itemRequest) {
        validationUpdatedItem(itemId, ownerId);
        Item item = initializeItem(ownerId, itemRequest);

        Item updatedItem = itemDao.updatedItem(itemId, item);

        return ItemMapper.toItemDto(updatedItem);
    }

    private void validationUpdatedItem(Long itemId, Long ownerId) {
        Optional<Item> optItem = itemDao.getItem(itemId);
        if (optItem.isPresent()) {
            Item item = optItem.get();

            if (!ownerId.equals(item.getOwner())) {
                log.warn("Пользователь с id = {} хотел изменить товар с id = {} ", ownerId, itemId);
                throw new NotFoundException("Изменить товар может только его владелец");
            }
        }
    }

    private Item initializeItem(Long ownerId, ItemRequestDto itemRequest) {
        userService.findUser(ownerId);
        return ItemMapper.toItem(ownerId, itemRequest);
    }
}
