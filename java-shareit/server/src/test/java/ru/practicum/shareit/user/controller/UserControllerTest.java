package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ShareItServer.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    void findUser_success() throws Exception {
        long userId = 1L;
        UserResponseDto response = new UserResponseDto(userId, "Иван", "ivan@mail.ru");

        when(userService.findUser(userId)).thenReturn(response);

        mvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Иван"));

        verify(userService).findUser(userId);
    }

    @Test
    void createUser_success() throws Exception {
        UserRequestDto request = new UserRequestDto();
        request.setName("Петр");
        request.setEmail("petr@mail.ru");

        UserResponseDto response = new UserResponseDto(1L, request.getName(), request.getEmail());

        when(userService.create(any(UserRequestDto.class))).thenReturn(response);

        mvc.perform(post("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Петр"));

        verify(userService).create(any(UserRequestDto.class));
    }

    @Test
    void updateUser_success() throws Exception {
        long userId = 2L;
        UserRequestDto update = new UserRequestDto();
        update.setName("Новое имя");
        update.setEmail("new@mail.ru");

        UserResponseDto response = new UserResponseDto(userId, update.getName(), update.getEmail());
        when(userService.updated(eq(userId), any(UserRequestDto.class))).thenReturn(response);

        mvc.perform(patch("/users/{userId}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Новое имя"));

        verify(userService).updated(eq(userId), any(UserRequestDto.class));
    }

    @Test
    void deleteUser_success() throws Exception {
        long userId = 1L;
        mvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isNoContent());
        verify(userService).deleteUser(userId);
    }
}
