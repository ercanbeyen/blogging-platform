package com.ercanbeyen.bloggingplatform.dto.converter;

import com.ercanbeyen.bloggingplatform.dto.CommentDto;
import com.ercanbeyen.bloggingplatform.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentDtoConverter {
    public CommentDto convert(Comment comment) {
        return CommentDto.builder()
                .author(comment.getAuthor())
                .text(comment.getText())
                .latestChangeAt(comment.getLatestChangeAt())
                .build();
    }
}
