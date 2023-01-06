package com.ercanbeyen.bloggingplatform.dto.converter;

import com.ercanbeyen.bloggingplatform.dto.AuthorDto;
import com.ercanbeyen.bloggingplatform.entity.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorDtoConverter {
    public AuthorDto convert(Author author) {
        return AuthorDto.builder()
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .username(author.getUsername())
                .about(author.getAbout())
                .email(author.getEmail())
                .gender(author.getGender())
                .location(author.getLocation())
                .favoriteTopics(author.getFavoriteTopics())
                .build();
    }
}
