package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

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
public class ItemControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemService itemService;

    @Test
    void getItems_success() throws Exception {
        List<ItemFullResponseDto> list = Collections.emptyList();

        when(itemService.findAll(1L)).thenReturn(list);

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(itemService).findAll(1L);
    }

    @Test
    void getItems_withoutUserId_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/items"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findItemById_success() throws Exception {
        long itemId = 1L;

        ItemFullResponseDto response = new ItemFullResponseDto(itemId, "Дрель", "Ударная",
                true, null, null, null, null);

        when(itemService.findItemById(1L, itemId)).thenReturn(response);

        mvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId));

        verify(itemService).findItemById(1L, itemId);
    }

    @Test
    void findItemById_withoutUserId_shouldReturnBadRequest() throws Exception {
        long itemId = 1L;

        mvc.perform(get("/items/{itemId}", itemId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void searchItems_success() throws Exception {
        String text = "дрель";

        List<ItemResponseDto> list = Collections.emptyList();

        when(itemService.searchItems(text)).thenReturn(list);

        mvc.perform(get("/items/search")
                        .param("text", text))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(itemService).searchItems(text);
    }

    @Test
    void searchItems_withoutTextParam_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/items/search"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createItem_success() throws Exception {
        ItemRequestDto request = new ItemRequestDto();
        request.setName("Дрель");
        request.setDescription("Ударная");
        request.setAvailable(true);

        ItemResponseDto response = new ItemResponseDto(1L, request.getName(), request.getDescription(),
                request.getAvailable(), null);

        when(itemService.create(eq(1L),
                any(ItemRequestDto.class))).thenReturn(response);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(itemService).create(eq(1L), any(ItemRequestDto.class));
    }

    @Test
    void createItem_withoutUserId_shouldReturnBadRequest() throws Exception {
        ItemRequestDto request = new ItemRequestDto();
        request.setName("Дрель");
        request.setDescription("Ударная");
        request.setAvailable(true);

        mvc.perform(post("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createComment_success() throws Exception {
        long itemId = 1L;
        CommentRequestDto request = new CommentRequestDto();
        request.setText("Отличная вещь!");

        CommentResponseDto response = new CommentResponseDto(1L, request.getText(), 123L, 125L,
                "Иван", LocalDateTime.now());


        when(itemService.createComment(eq(1L), eq(itemId),
                any(CommentRequestDto.class))).thenReturn(response);

        mvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
        verify(itemService).createComment(eq(1L), eq(itemId), any(CommentRequestDto.class));
    }

    @Test
    void createComment_withoutUserId_shouldReturnBadRequest() throws Exception {
        long itemId = 1L;
        CommentRequestDto request = new CommentRequestDto();
        request.setText("Ok");

        mvc.perform(post("/items/{itemId}/comment", itemId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateItem_success() throws Exception {
        long itemId = 1L;
        ItemRequestDto update = new ItemRequestDto();
        update.setName("Новое имя");

        ItemResponseDto response = new ItemResponseDto(itemId, update.getName(),
                null, null, null);

        when(itemService.updated(eq(itemId), eq(1L),
                any(ItemRequestDto.class))).thenReturn(response);

        mvc.perform(patch("/items/{id}", itemId)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId));

        verify(itemService).updated(eq(itemId), eq(1L), any(ItemRequestDto.class));
    }

    @Test
    void updateItem_withoutUserId_shouldReturnBadRequest() throws Exception {
        long itemId = 1L;

        mvc.perform(patch("/items/{id}", itemId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
