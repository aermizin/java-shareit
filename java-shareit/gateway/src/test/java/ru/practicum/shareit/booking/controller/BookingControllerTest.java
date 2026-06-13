package ru.practicum.shareit.booking.controller;

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
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ShareItGateway.class)
@AutoConfigureMockMvc
public class BookingControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookingClient bookingClient;

    private BookingRequestDto requestDto;

    @Test
    void getBookings_shouldUseDefaultParams() throws Exception {
        when(bookingClient.getBookings(1L, BookingState.ALL, 0, 10))
                .thenReturn(ResponseEntity.ok("[]"));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingClient).getBookings(1L, BookingState.ALL, 0, 10);
    }

    @Test
    void getBookings_shouldUseCustomParams() throws Exception {
        when(bookingClient.getBookings(1L, BookingState.CURRENT, 5, 20))
                .thenReturn(ResponseEntity.ok("[]"));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "current")
                        .param("from", "5")
                        .param("size", "20"))
                .andExpect(status().isOk());

        verify(bookingClient).getBookings(1L, BookingState.CURRENT, 5, 20);
    }

    @Test
    void getBookings_withInvalidState_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Unknown state: invalid"));
    }

    @Test
    void getBookings_withNegativeFrom_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBookings_withNegativeSize_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("size", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBookings_withZeroSize_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("size", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBookings_withoutUserIdHeader_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOwnerBookings_shouldUseDefaultParams() throws Exception {
        when(bookingClient.getOwnerBookings(1L, BookingState.ALL))
                .thenReturn(ResponseEntity.ok("[]"));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingClient).getOwnerBookings(1L, BookingState.ALL);
    }

    @Test
    void getOwnerBookings_shouldUseCustomParams() throws Exception {
        when(bookingClient.getOwnerBookings(1L, BookingState.CURRENT))
                .thenReturn(ResponseEntity.ok("[]"));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "current")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingClient).getOwnerBookings(1L, BookingState.CURRENT);
    }

    @Test
    void getOwnerBookings_withInvalidState_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Unknown state: invalid"));
    }

    @Test
    void getOwnerBookings_withoutUserIdHeader_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/bookings/owner"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBooking_success() throws Exception {
        long bookingId = 10L;
        when(bookingClient.getBooking(1L, bookingId))
                .thenReturn(ResponseEntity.ok("{}"));

        mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingClient).getBooking(1L, bookingId);
    }

    @Test
    void getBooking_withNegativeBookingId_shouldReturnBadRequest() throws Exception {
        long bookingId = -10L;
        mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBooking_withZeroBookingId_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/bookings/{bookingId}", 0)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBooking_withoutUserIdHeader_shouldReturnBadRequest() throws Exception {
        long bookingId = 10L;
        mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBooking_success() throws Exception {
        requestDto = new BookingRequestDto();
        requestDto.setItemId(1L);
        requestDto.setStart(Instant.now().plusSeconds(5));
        requestDto.setEnd(Instant.now().plusSeconds(10));

        when(bookingClient.createBooking(eq(1L), any(BookingRequestDto.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("{}"));

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());

        verify(bookingClient).createBooking(eq(1L), any(BookingRequestDto.class));
    }

    @Test
    void createBooking_withPastStart_shouldReturnBadRequest() throws Exception {
        requestDto = new BookingRequestDto();
        requestDto.setItemId(1L);
        requestDto.setStart(Instant.now().minusSeconds(5));
        requestDto.setEnd(Instant.now().plusSeconds(5));

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBooking_withPastEnd_shouldReturnBadRequest() throws Exception {
        requestDto = new BookingRequestDto();
        requestDto.setItemId(1L);
        requestDto.setStart(Instant.now().plusSeconds(5));
        requestDto.setEnd(Instant.now().minusSeconds(5));

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBooking_withoutUserIdHeader_shouldReturnBadRequest() throws Exception {
        BookingRequestDto request = new BookingRequestDto();
        request.setItemId(1L);
        request.setStart(Instant.now().plusSeconds(5));
        request.setEnd(Instant.now().plusSeconds(10));

        mvc.perform(post("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBookingStatus_success() throws Exception {
        long bookingId = 10L;
        boolean approved = true;
        when(bookingClient.updateStatus(1L, bookingId, approved))
                .thenReturn(ResponseEntity.ok("{}"));

        mvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingClient).updateStatus(1L, bookingId, approved);
    }

    @Test
    void updateBookingStatus_withZeroBookingId_shouldReturnBadRequest() throws Exception {
        mvc.perform(patch("/bookings/{bookingId}", 0)
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBookingStatus_withNegativeBookingId_shouldReturnBadRequest() throws Exception {
        mvc.perform(patch("/bookings/{bookingId}", -5)
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBookingStatus_withMissingApprovedParam_shouldReturnBadRequest() throws Exception {
        mvc.perform(patch("/bookings/{bookingId}", 10)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBookingStatus_withInvalidApprovedValue_shouldReturnBadRequest() throws Exception {
        mvc.perform(patch("/bookings/{bookingId}", 10)
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "not-boolean"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBookingStatus_withoutUserIdHeader_shouldReturnBadRequest() throws Exception {
        mvc.perform(patch("/bookings/{bookingId}", 10)
                        .param("approved", "true"))
                .andExpect(status().isBadRequest());
    }
}
