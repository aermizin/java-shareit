package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ShareItGateway.class)
@AutoConfigureMockMvc
public class ItemControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemClient itemClient;

    private ItemRequestDto itemRequestDto;

    @Test
    void getItems_success() throws Exception {
        when(itemClient.getItems(1L)).thenReturn(ResponseEntity.ok("[]"));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(itemClient).getItems(1L);
    }

    @Test
    void getItems_withoutUserIdHeader_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/items"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getItem_success() throws Exception {
        long itemId = 10L;
        when(itemClient.getItem(1L, itemId)).thenReturn(ResponseEntity.ok("{}"));

        mvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(itemClient).getItem(1L, itemId);
    }

    @Test
    void getItem_withZeroItemId_shouldReturnBadRequest() throws Exception {
        long itemId = 0L;
        mvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getItem_withNegativeItemId_shouldReturnBadRequest() throws Exception {
        long itemId = -1L;
        mvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getItem_withoutUserIdHeader_shouldReturnBadRequest() throws Exception {
        long itemId = 1L;
        mvc.perform(get("/items/{itemId}", itemId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findItemsByText_success() throws Exception {
        String text = "дрель";
        when(itemClient.findItemsByText(1L, text)).thenReturn(ResponseEntity.ok("[]"));

        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", text))
                .andExpect(status().isOk());

        verify(itemClient).findItemsByText(1L, text);
    }

    @Test
    void findItemsByText_withEmptyText_shouldReturnEmptyList() throws Exception {
        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", ""))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verifyNoInteractions(itemClient);
    }

    @Test
    void findItemsByText_withBlankText_shouldReturnEmptyList() throws Exception {
        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", "   "))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verifyNoInteractions(itemClient);
    }

    @Test
    void findItemsByText_withoutUserIdHeader_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/items/search")
                        .param("text", "дрель"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findItemsByText_withoutTextParam_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createItem_success() throws Exception {
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setName("Valid name");
        itemRequestDto.setDescription("Valid description");
        itemRequestDto.setAvailable(false);
        itemRequestDto.setRequestId(null);

        when(itemClient.createItem(eq(1L), any(ItemRequestDto.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("{}"));

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isCreated());

        verify(itemClient).createItem(eq(1L), any(ItemRequestDto.class));
    }

    @Test
    void createItem_withBlankName_shouldReturnBadRequest() throws Exception {
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setName("");
        itemRequestDto.setDescription("Valid description");
        itemRequestDto.setAvailable(true);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createItem_withNullAvailable_shouldReturnBadRequest() throws Exception {
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setName("Valid name");
        itemRequestDto.setDescription("Valid description");
        itemRequestDto.setAvailable(null);
        itemRequestDto.setRequestId(123L);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createItem_withEmptyBody_shouldReturnBadRequest() throws Exception {
        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createItem_withoutUserIdHeader_shouldReturnBadRequest() throws Exception {
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setName("Valid name");
        itemRequestDto.setDescription("Valid description");
        itemRequestDto.setAvailable(true);

        mvc.perform(post("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createComment_success() throws Exception {
        long itemId = 1L;
        CommentRequestDto request = new CommentRequestDto();
        request.setText("Отличная вещь!");

        when(itemClient.createComment(eq(1L), eq(itemId), any(CommentRequestDto.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("{}"));

        mvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(itemClient).createComment(eq(1L), eq(itemId), any(CommentRequestDto.class));
    }

    @Test
    void createComment_withBlankText_shouldReturnBadRequest() throws Exception {
        long itemId = 1L;
        CommentRequestDto request = new CommentRequestDto();
        request.setText("");

        mvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createComment_withOnlySpacesText_shouldReturnBadRequest() throws Exception {
        long itemId = 1L;
        CommentRequestDto request = new CommentRequestDto();
        request.setText("   ");

        mvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createComment_withZeroItemId_shouldReturnBadRequest() throws Exception {
        long itemId = 0L;
        CommentRequestDto request = new CommentRequestDto();
        request.setText("Отличная вещь!");

        mvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createComment_withNegativeItemId_shouldReturnBadRequest() throws Exception {
        long itemId = -1L;
        CommentRequestDto request = new CommentRequestDto();
        request.setText("Отличная вещь!");

        mvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createComment_withoutUserIdHeader_shouldReturnBadRequest() throws Exception {
        long itemId = 1L;
        CommentRequestDto request = new CommentRequestDto();
        request.setText("Отличная вещь!");

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
        ItemRequestDto updateRequest = new ItemRequestDto();
        updateRequest.setName("New name");
        updateRequest.setDescription("New description");
        updateRequest.setAvailable(false);

        when(itemClient.updateItem(eq(1L), eq(itemId), any(ItemRequestDto.class)))
                .thenReturn(ResponseEntity.ok("{}"));

        mvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        verify(itemClient).updateItem(eq(1L), eq(itemId), any(ItemRequestDto.class));
    }

    @Test
    void updateItem_withZeroItemId_shouldReturnBadRequest() throws Exception {
        long itemId = 0L;
        ItemRequestDto updateRequest = new ItemRequestDto();
        updateRequest.setName("New name");
        updateRequest.setDescription("New description");
        updateRequest.setAvailable(false);

        mvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateItem_withNegativeItemId_shouldReturnBadRequest() throws Exception {
        long itemId = -5L;
        mvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateItem_withoutUserIdHeader_shouldReturnBadRequest() throws Exception {
        long itemId = 1L;
        mvc.perform(patch("/items/{itemId}", itemId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
