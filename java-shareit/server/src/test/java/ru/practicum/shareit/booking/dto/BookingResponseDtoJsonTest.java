package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingResponseDtoJsonTest {
    @Autowired
    private JacksonTester<BookingResponseDto> json;

    ItemResponseDto item = new ItemResponseDto(1L, "Дрель", null, true, null);
    UserResponseDto booker = new UserResponseDto(2L, "Иван", "ivan@mail.ru");

    @Test
    void serialize_correctDateFormat() throws Exception {

        BookingResponseDto dto = new BookingResponseDto(
                100L,
                Instant.parse("2025-06-15T02:00:00Z"),
                Instant.parse("2025-06-20T11:00:00Z"),
                item,
                booker,
                BookingStatus.APPROVED
        );

        JsonContent<BookingResponseDto> result = json.write(dto);

        assertThat(result).hasJsonPathStringValue("$.start", "2025-06-15T09:00:00");
        assertThat(result).hasJsonPathStringValue("$.end", "2025-06-20T18:00:00");

        assertThat(result).hasJsonPathNumberValue("$.id");
        assertThat(result).hasJsonPath("$.item");
        assertThat(result).hasJsonPath("$.booker");
        assertThat(result).hasJsonPathStringValue("$.status");
    }
}
