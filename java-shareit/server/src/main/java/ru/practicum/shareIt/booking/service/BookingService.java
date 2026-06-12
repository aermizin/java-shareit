package ru.practicum.shareIt.booking.service;

import ru.practicum.shareIt.booking.dto.BookingRequestDto;
import ru.practicum.shareIt.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto getBooking(Long userId, Long bookingId);

    List<BookingResponseDto> getBookings(Long bookerId, String status);

    List<BookingResponseDto> getOwnerBookings(Long ownerId, String status);

    BookingResponseDto create(Long bookerId, BookingRequestDto newBooking);

    BookingResponseDto updateStatus(Long userId, Long bookingId, boolean status);
}


