package com.ercanbeyen.bloggingplatform.dto.converter;

import com.ercanbeyen.bloggingplatform.dto.PostDto;
import com.ercanbeyen.bloggingplatform.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostDtoConverter {
    public PostDto convert(Post post) {
        return PostDto.builder()
                .author(post.getAuthor())
                .title(post.getTitle())
                .text(post.getText())
                .category(post.getCategory())
                .numberOfLikes(post.getNumberOfLikes())
                .tags(post.getTags())
                .comments(post.getComments())
                .latestChangeAt(post.getLatestChangeAt())
                .build();
    }
}
