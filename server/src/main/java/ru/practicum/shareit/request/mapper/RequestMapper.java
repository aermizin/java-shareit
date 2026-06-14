package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.dto.RequestWithItemsResponseDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class RequestMapper {
    public static RequestWithItemsResponseDto toRequestWithItemsDto(Request request, List<Item> items) {
        return new RequestWithItemsResponseDto(
                request.getId(),
                request.getDescription(),
                request.getCreated(),
                items != null ? items.stream()
                        .map(ItemMapper::toItemShortDto).collect(Collectors.toList()) : Collections.emptyList()
        );
    }

    public static RequestResponseDto toRequestDto(Request request) {
        return new RequestResponseDto(
                request.getId(),
                request.getDescription(),
                request.getRequestor(),
                request.getCreated()
        );
    }

    public static Request toRequest(RequestDto requestDto, User requestor) {
        Request request = new Request();
        request.setDescription(requestDto.getDescription());
        request.setRequestor(requestor);
        return request;
    }
}

