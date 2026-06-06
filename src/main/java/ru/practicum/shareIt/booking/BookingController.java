package ru.practicum.shareIt.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareIt.booking.dto.BookingRequestDto;
import ru.practicum.shareIt.booking.dto.BookingResponseDto;
import ru.practicum.shareIt.booking.service.BookingService;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable Long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> getBookings(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                                @RequestParam(defaultValue = "ALL") String status) {
        return bookingService.getBookings(bookerId, status);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                     @RequestParam(defaultValue = "ALL") String status) {
        return bookingService.getOwnerBookings(ownerId, status);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto createBooking(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                            @Valid @RequestBody BookingRequestDto newBooking) {
        return bookingService.create(bookerId, newBooking);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto updateBookingStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @PathVariable Long bookingId,
                                                  @RequestParam boolean approved) {
        return bookingService.updateStatus(userId, bookingId, approved);
    }
}
