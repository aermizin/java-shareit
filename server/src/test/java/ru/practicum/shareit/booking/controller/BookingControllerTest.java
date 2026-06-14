package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ShareItServer.class)
@AutoConfigureMockMvc
public class BookingControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookingService bookingService;

    @Test
    void getBooking_success() throws Exception {
        long bookingId = 10L;

        BookingResponseDto response = new BookingResponseDto(bookingId, Instant.now(),
                Instant.now().plusSeconds(5), null, null, null);

        when(bookingService.getBooking(1L, bookingId)).thenReturn(response);

        mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingId));

        verify(bookingService).getBooking(1L, bookingId);
    }

    @Test
    void getBooking_withoutUserId_shouldReturnBadRequest() throws Exception {
        long bookingId = 1L;
        mvc.perform(get("/bookings/{bookingId}", bookingId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBookings_withDefaultStatus() throws Exception {
        List<BookingResponseDto> list = List.of();
        when(bookingService.getBookings(1L, "ALL")).thenReturn(list);

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(bookingService).getBookings(1L, "ALL");
    }

    @Test
    void getBookings_withCustomStatus() throws Exception {
        when(bookingService.getBookings(1L, "CURRENT")).thenReturn(List.of());

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("status", "CURRENT"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(bookingService).getBookings(1L, "CURRENT");
    }

    @Test
    void getBookings_withoutUserId_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/bookings"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOwnerBookings_success() throws Exception {
        when(bookingService.getOwnerBookings(1L, "ALL")).thenReturn(List.of());

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(bookingService).getOwnerBookings(1L, "ALL");
    }

    @Test
    void getOwnerBookings_withoutUserId_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/bookings/owner"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBooking_success() throws Exception {
        BookingRequestDto request = new BookingRequestDto();
        request.setItemId(1L);
        request.setStart(Instant.now().plusSeconds(5));
        request.setEnd(Instant.now().plusSeconds(10));

        BookingResponseDto response = new BookingResponseDto(1L, request.getStart(),
                request.getEnd(), null, null, null);

        when(bookingService.create(eq(1L), any(BookingRequestDto.class))).thenReturn(response);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(bookingService).create(eq(1L), any(BookingRequestDto.class));
    }

    @Test
    void createBooking_withoutUserId_shouldReturnBadRequest() throws Exception {
        BookingRequestDto request = new BookingRequestDto();
        request.setItemId(1L);
        request.setStart(Instant.now().minusSeconds(5));
        request.setEnd(Instant.now().plusSeconds(10));

        mvc.perform(post("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBookingStatus_success() throws Exception {
        long bookingId = 1L;
        BookingResponseDto response = new BookingResponseDto(bookingId, null,
                null, null, null, null);

        when(bookingService.updateStatus(1L, bookingId, true)).thenReturn(response);

        mvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingId));

        verify(bookingService).updateStatus(1L, bookingId, true);
    }

    @Test
    void updateBookingStatus_missingApprovedParam_shouldReturnBadRequest() throws Exception {
        long bookingId = 1L;
        mvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBookingStatus_withInvalidApproved_shouldReturnBadRequest() throws Exception {
        mvc.perform(patch("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "not-boolean"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBookingStatus_withoutUserId_shouldReturnBadRequest() throws Exception {
        mvc.perform(patch("/bookings/{bookingId}", 1)
                        .param("approved", "true"))
                .andExpect(status().isBadRequest());
    }
}
