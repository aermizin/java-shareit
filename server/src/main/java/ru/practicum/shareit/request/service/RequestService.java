package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.dto.RequestWithItemsResponseDto;
import ru.practicum.shareit.request.repository.projection.RequestShortProjection;

import java.util.List;

public interface RequestService {
    List<RequestWithItemsResponseDto> getOwnRequests(long userId);

    List<RequestShortProjection> getAllRequests(long userId);

    RequestWithItemsResponseDto getRequestById(long userId, Long requestId);

    RequestResponseDto createRequest(long userId, RequestDto requestDto);
}

