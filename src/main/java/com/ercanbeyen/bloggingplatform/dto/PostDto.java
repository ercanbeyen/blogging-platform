package com.ercanbeyen.bloggingplatform.dto;

import com.ercanbeyen.bloggingplatform.document.Comment;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostDto {
    private String authorId;
    private String title;
    private String text;
    private String category;
    private int numberOfLikes;
    private List<String> tags;
    private List<Comment> comments;
    private LocalDateTime latestChangeAt;
}
