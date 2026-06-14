package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.dto.RequestWithItemsResponseDto;
import ru.practicum.shareit.request.repository.projection.RequestShortProjection;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class RequestController {
    private final RequestService requestService;

    @GetMapping
    public List<RequestWithItemsResponseDto> getOwnRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.getOwnRequests(userId);
    }

    @GetMapping("/all")
    public List<RequestShortProjection> getAllRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public RequestWithItemsResponseDto getRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                      @PathVariable Long requestId) {
        return requestService.getRequestById(userId, requestId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestResponseDto createRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestBody RequestDto requestDto) {
        return requestService.createRequest(userId, requestDto);
    }
}

