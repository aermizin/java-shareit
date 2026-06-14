package ru.practicum.shareit.user;

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
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.dto.UserRequestDto;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ShareItGateway.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserClient userClient;

    private UserRequestDto userDto;

    @Test
    void getUser_success() throws Exception {
        long userId = 1L;
        when(userClient.getUser(userId)).thenReturn(ResponseEntity.ok("{}"));
        mvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk());

        verify(userClient).getUser(userId);
    }

    @Test
    void getUser_withZeroUserId_shouldReturnBadRequest() throws Exception {
        long userId = 0L;
        mvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUser_withNegativeUserId_shouldReturnBadRequest() throws Exception {
        long userId = -1L;
        mvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_success() throws Exception {
        UserRequestDto dto = new UserRequestDto();
        dto.setName("Иван");
        dto.setEmail("valid@mail.com");

        when(userClient.createUser(any(UserRequestDto.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("{}"));

        mvc.perform(post("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(userClient).createUser(any(UserRequestDto.class));
    }

    @Test
    void createUser_withBlankName_shouldReturnBadRequest() throws Exception {
        UserRequestDto dto = new UserRequestDto();
        dto.setName("");
        dto.setEmail("valid@mail.com");
        mvc.perform(post("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_withInvalidEmail_shouldReturnBadRequest() throws Exception {
        UserRequestDto dto = new UserRequestDto();
        dto.setName("Иван");
        dto.setEmail("not valid email");

        mvc.perform(post("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_success() throws Exception {
        long userId = 1L;
        UserRequestDto dto = new UserRequestDto();
        dto.setName("New name");

        when(userClient.updateUser(eq(userId), any(UserRequestDto.class)))
                .thenReturn(ResponseEntity.ok("{}"));

        mvc.perform(patch("/users/{userId}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(userClient).updateUser(eq(userId), any(UserRequestDto.class));
    }

    @Test
    void updateUser_withZeroUserId_shouldReturnBadRequest() throws Exception {
        long userId = 0L;
        mvc.perform(patch("/users/{userId}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_withNegativeUserId_shouldReturnBadRequest() throws Exception {
        long userId = -1L;
        mvc.perform(patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_withEmptyBody_shouldReturnBadRequest() throws Exception {
        long userId = 1L;
        mvc.perform(patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser_success() throws Exception {
        long userId = 1L;
        when(userClient.deleteUser(userId)).thenReturn(ResponseEntity.noContent().build());

        mvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isNoContent());

        verify(userClient).deleteUser(userId);
    }

    @Test
    void deleteUser_withZeroUserId_shouldReturnBadRequest() throws Exception {
        long userId = 0L;
        mvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser_withNegativeUserId_shouldReturnBadRequest() throws Exception {
        long userId = -1L;
        mvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isBadRequest());
    }
}
