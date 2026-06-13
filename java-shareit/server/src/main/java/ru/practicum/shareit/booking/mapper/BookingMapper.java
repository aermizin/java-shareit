package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {

    public static Booking toBooking(BookingRequestDto newBooking, Item item, User booker) {
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

