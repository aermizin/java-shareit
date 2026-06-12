package ru.practicum.shareIt.booking.mapper;

import jakarta.persistence.*;
import ru.practicum.shareIt.booking.dto.BookingRequestDto;
import ru.practicum.shareIt.booking.dto.BookingResponseDto;
import ru.practicum.shareIt.booking.model.Booking;
import ru.practicum.shareIt.booking.model.BookingStatus;
import ru.practicum.shareIt.item.dto.ItemResponseDto;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.user.dto.UserResponseDto;
import ru.practicum.shareIt.user.model.User;

public class BookingMapper {

    public static Booking toBooking(BookingRequestDto newBooking, Item item,User booker) {
        return Booking.builder()
                .start(newBooking.getStart())
                .end(newBooking.getEnd())
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();
    }

    public static BookingResponseDto toBookingDto(Booking booking) {
        Item item = booking.getItem();
        User user = booking.getBooker();

        return new BookingResponseDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                new ItemResponseDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                        item.getRequest() != null ? item.getRequest().getId() : null),
                new UserResponseDto(user.getId(), user.getName(), user.getEmail()),
                booking.getStatus()
        );
    }
}
