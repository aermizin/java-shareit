package ru.practicum.shareIt.item.mapper;

import ru.practicum.shareIt.item.dto.CommentResponseDto;
import ru.practicum.shareIt.item.model.Comment;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static CommentResponseDto toCommentDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getText(),
                comment.getItem().getId(),
                comment.getAuthor().getId(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public static Comment toComment(Item item, User author, String text) {
        return Comment.builder()
                .text(text)
                .item(item)
                .author(author)
                .created(LocalDateTime.now())
                .build();
    }
}
