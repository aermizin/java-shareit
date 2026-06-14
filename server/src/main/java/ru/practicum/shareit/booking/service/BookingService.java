package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto getBooking(long userId, Long bookingId);

    List<BookingResponseDto> getBookings(long userId, String status);

    List<BookingResponseDto> getOwnerBookings(long userId, String status);

    BookingResponseDto create(long userId, BookingRequestDto newBooking);

    BookingResponseDto updateStatus(long userId, Long bookingId, boolean status);
}



