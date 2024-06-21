package com.ercanbeyen.bloggingplatform.dto.converter;

import com.ercanbeyen.bloggingplatform.dto.PostDto;
import com.ercanbeyen.bloggingplatform.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostDtoConverter {
    public PostDto convert(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .authorId(post.getAuthor().getId())
                .title(post.getTitle())
                .text(post.getText())
                .category(post.getCategory())
                .tags(post.getTags())
                .latestChangeAt(post.getLatestChangeAt())
                .build();
    }
}
