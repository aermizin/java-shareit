package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.dto.RequestWithItemsResponseDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.repository.projection.RequestShortProjection;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<RequestWithItemsResponseDto> getOwnRequests(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным ID не найден"));

        List<Request> requests = requestRepository.findByRequestorIdOrderByCreatedDesc(userId);

        if (requests.isEmpty()) {
            log.warn("Не найдено ни одного запроса у пользователя с ID = {}", userId);
            return List.of();
        }
        List<Long> requestIds = requests.stream().map(Request::getId).toList();

        Map<Long, List<Item>> itemsByRequestMap = itemRepository.findByRequestIdIn(requestIds).stream()
                .collect(Collectors.groupingBy(item -> item.getRequest().getId()));

        return requests.stream()
                .map(request -> {
                    List<Item> items = itemsByRequestMap.get(request.getId());
                    return RequestMapper.toRequestWithItemsDto(request, items);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestShortProjection> getAllRequests(long userId) {
        return requestRepository.findByRequestor_IdNotOrderByCreatedDesc(userId);
    }

    @Override
    public RequestWithItemsResponseDto getRequestById(long userId, Long requestId) {
        Request request = requestRepository.findByIdWithItems(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с указанным ID не найден"));

        return RequestMapper.toRequestWithItemsDto(request, request.getItems());
    }

    @Override
    @Transactional
    public RequestResponseDto createRequest(long userId, RequestDto requestDto) {
        User requestor = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным ID не найден"));

        Request request = RequestMapper.toRequest(requestDto, requestor);
        Request saved = requestRepository.save(request);
        log.info("Создан новый запрос с id = {}", saved.getId());
        return RequestMapper.toRequestDto(saved);
    }
}
