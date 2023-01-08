package com.ercanbeyen.bloggingplatform.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    private String author;
    private String postId;
    private String text;
    LocalDateTime latestChangeAt;
}
