package com.ercanbeyen.bloggingplatform.dto.converter;

import com.ercanbeyen.bloggingplatform.dto.PostDto;
import com.ercanbeyen.bloggingplatform.document.Post;
import org.springframework.stereotype.Component;

@Component
public class PostDtoConverter {
    public PostDto convert(Post post) {
        return PostDto.builder()
                .authorId(post.getAuthor().getId())
                .title(post.getTitle())
                .text(post.getText())
                .category(post.getCategory())
                .tags(post.getTags())
                .comments(post.getComments())
                .latestChangeAt(post.getLatestChangeAt())
                .build();
    }
}
