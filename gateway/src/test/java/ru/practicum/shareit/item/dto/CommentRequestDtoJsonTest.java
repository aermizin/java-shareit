package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentRequestDtoJsonTest {

    @Autowired
    private JacksonTester<CommentRequestDto> json;

    @Test
    void serialize_success() throws Exception {
        CommentRequestDto dto = new CommentRequestDto("Отличная вещь");

        assertThat(json.write(dto))
                .hasJsonPathValue("$.text", "Отличная вещь");

    }

    @Test
    void deserialize_success() throws Exception {
        String content = "{ \"text\": \"Отличная вещь\" }";
        CommentRequestDto dto = json.parse(content).getObject();
        assertThat(dto.getText()).isEqualTo("Отличная вещь");
    }
}
