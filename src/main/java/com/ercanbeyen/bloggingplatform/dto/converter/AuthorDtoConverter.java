package com.ercanbeyen.bloggingplatform.dto.converter;

import com.ercanbeyen.bloggingplatform.dto.AuthorDto;
import com.ercanbeyen.bloggingplatform.document.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorDtoConverter {
    public AuthorDto convert(Author author) {
        return AuthorDto.builder()
                .id(author.getId())
                .username(author.getUsername())
                .email(author.getEmail())
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .username(author.getUsername())
                .roles(author.getRoles())
                .about(author.getAbout())
                .email(author.getEmail())
                .gender(author.getGender())
                .location(author.getLocation())
                .favoriteTopics(author.getFavoriteTopics())
                .createdAt(author.getCreatedAt())
                .build();
    }
}
