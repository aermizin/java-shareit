package ru.practicum.shareit.request;

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
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.request.RequestClient;
import ru.practicum.shareit.request.dto.RequestDto;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ShareItGateway.class)
@AutoConfigureMockMvc
public class RequestControllerTest  {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RequestClient requestClient;

    private ItemRequestDto requestDto;

    @Test
    void getOwnRequests_success() throws Exception {
        when(requestClient.getOwnRequests(1L)).thenReturn(ResponseEntity.ok("[]"));
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(requestClient).getOwnRequests(1L);
    }

    @Test
    void getOwnRequests_withoutUserIdHeader_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/requests"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRequests_all_success() throws Exception {
        when(requestClient.getRequests(1L)).thenReturn(ResponseEntity.ok("[]"));
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(requestClient).getRequests(1L);
    }

    @Test
    void getRequests_all_withoutUserIdHeader_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/requests/all"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRequest_success() throws Exception {
        long requestId = 5L;
        when(requestClient.getRequest(1L, requestId)).thenReturn(ResponseEntity.ok("{}"));

        mvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(requestClient).getRequest(1L, requestId);
    }

    @Test
    void getRequest_withZeroRequestId_shouldReturnBadRequest() throws Exception {
        long requestId = 0L;
        mvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRequest_withNegativeRequestId_shouldReturnBadRequest() throws Exception {
        long requestId = -1L;
        mvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRequest_withoutUserIdHeader_shouldReturnBadRequest() throws Exception {
        long requestId = 1L;
        mvc.perform(get("/requests/{requestId}", requestId))
                .andExpect(status().isBadRequest());
    }

    // POST /requests
    @Test
    void createRequest_success() throws Exception {
        RequestDto requestDto = new RequestDto();
        requestDto.setDescription("Нужна дрель");

        when(requestClient.createRequest(eq(1L), any(RequestDto.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("{}"));

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());

        verify(requestClient).createRequest(eq(1L), any(RequestDto.class));
    }

    @Test
    void createRequest_withBlankDescription_shouldReturnBadRequest() throws Exception {
        RequestDto requestDto = new RequestDto();
        requestDto.setDescription("");

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRequest_withDescriptionOnlySpaces_shouldReturnBadRequest() throws Exception {
        RequestDto requestDto = new RequestDto();
        requestDto.setDescription("   ");

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRequest_withEmptyBody_shouldReturnBadRequest() throws Exception {
        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRequest_withDescriptionIsNull_shouldReturnBadRequest() throws Exception {
        RequestDto requestDto = new RequestDto();
        requestDto.setDescription(null);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRequest_withoutUserIdHeader_shouldReturnBadRequest() throws Exception {
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
