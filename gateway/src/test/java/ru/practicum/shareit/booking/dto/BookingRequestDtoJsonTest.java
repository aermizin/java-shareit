package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@JsonTest
public class BookingRequestDtoJsonTest {

    @Autowired
    private JacksonTester<BookingRequestDto> json;

    @Test
    void serialize_validDates_success() throws Exception {

        BookingRequestDto dto = new BookingRequestDto(
                101,
                Instant.parse("2025-06-15T09:00:00Z"),
                Instant.parse("2025-06-20T18:00:00Z")
        );

        assertThat(json.write(dto))
                .hasJsonPathValue("$.itemId", 101)
                .hasJsonPathValue("$.start", "2025-06-15T09:00:00")
                .hasJsonPathValue("$.end", "2025-06-20T18:00:00");
    }

    @Test
    void deserialize_validDates_success() throws Exception {
        String content = "{ \"itemId\": 101, \"start\": \"2025-06-15T09:00:00\", \"end\": \"2025-06-20T18:00:00\" }";

        BookingRequestDto dto = json.parse(content).getObject();
        assertThat(dto.getItemId()).isEqualTo(101);
        assertThat(dto.getStart()).isEqualTo(Instant.parse("2025-06-15T09:00:00Z"));
        assertThat(dto.getEnd()).isEqualTo(Instant.parse("2025-06-20T18:00:00Z"));
    }

    @Test
    void deserialize_missingStartAndEnd() throws Exception {
        String content = "{ \"itemId\": 123 }";
        BookingRequestDto dto = json.parse(content).getObject();
        assertThat(dto.getItemId()).isEqualTo(123);
        assertThat(dto.getStart()).isNull();
        assertThat(dto.getEnd()).isNull();
    }

    @Test
    void deserialize_invalidDateValue_throwsException() {
        String content = "{ \"itemId\": 1, \"start\": \"2025-13-45T99:00:00\", \"end\": \"2025-06-20T18:00:00\" }";
        assertThatThrownBy(() -> json.parse(content))
                .isInstanceOf(InvalidFormatException.class);
    }

    @Test
    void deserialize_startInvalidFormat_throwsException() {
        String content = "{ \"itemId\": \"123\", \"start\": \"not a date\", \"end\": \"2025-06-20T18:00:00\" }";
        assertThatThrownBy(() -> json.parse(content))
                .isInstanceOf(InvalidFormatException.class);
    }
}
