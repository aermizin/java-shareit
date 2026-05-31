package ru.practicum.shareIt.booking;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.user.User;

import java.time.LocalDateTime;

@Data
@Builder
public class Booking {
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Item item;

    private User booker;

    private BookingStatus status;
}
