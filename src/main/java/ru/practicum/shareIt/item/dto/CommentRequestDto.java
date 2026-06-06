package ru.practicum.shareIt.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequestDto {
    @NotBlank(message = "Комментарий не может быть null или пустым.")
    private String text;

}
