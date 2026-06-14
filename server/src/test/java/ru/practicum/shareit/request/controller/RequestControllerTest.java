package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.dto.RequestWithItemsResponseDto;
import ru.practicum.shareit.request.repository.projection.RequestShortProjection;
import ru.practicum.shareit.request.service.RequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ShareItServer.class)
@AutoConfigureMockMvc
public class RequestControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RequestService requestService;

    @Test
    void getOwnRequests_success() throws Exception {
        List<RequestWithItemsResponseDto> list = Collections.emptyList();

        when(requestService.getOwnRequests(1L)).thenReturn(list);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(requestService).getOwnRequests(1L);
    }

    @Test
    void getOwnRequests_withoutUserId_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/requests"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllRequests_success() throws Exception {
        List<RequestShortProjection> list = Collections.emptyList();

        when(requestService.getAllRequests(1L)).thenReturn(list);

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(requestService).getAllRequests(1L);
    }

    @Test
    void getAllRequests_withoutUserId_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/requests/all"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRequestById_success() throws Exception {
        long requestId = 10L;

        RequestWithItemsResponseDto response = new RequestWithItemsResponseDto(requestId, "Нужна дрель",
                null, null);
        when(requestService.getRequestById(1L, requestId)).thenReturn(response);

        mvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestId));

        verify(requestService).getRequestById(1L, requestId);
    }

    @Test
    void getRequestById_withoutUserId_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/requests/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRequest_success() throws Exception {
        RequestDto requestDto = new RequestDto();
        requestDto.setDescription("Нужна дрель");

        RequestResponseDto response = new RequestResponseDto(1L, requestDto.getDescription(),
                null, LocalDateTime.now());

        when(requestService.createRequest(eq(1L),
                any(RequestDto.class))).thenReturn(response);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(requestService).createRequest(eq(1L), any(RequestDto.class));
    }

    @Test
    void createRequest_withoutUserId_shouldReturnBadRequest() throws Exception {
        RequestDto requestDto = new RequestDto();
        requestDto.setDescription("Нужна дрель");

        mvc.perform(post("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }
}
