package ru.practicum.shareIt.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequestDto {

    @NotNull(message = "Id товара не может быть null.")
    private Long itemId;

    @NotNull(message = "Дата и время бронирование не может быть null.")
    private LocalDateTime start;

    @NotNull(message = "Дата и время окончания бронирования не могут быть null")
    private LocalDateTime end;

}
