package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
    AuthorDto createAuthor(AuthorDto authorDto);
    AuthorDto updateAuthor(String id, AuthorDto authorDto);
    AuthorDto getAuthor(String id);
    List<AuthorDto> getAuthors();
    void deleteAuthor(String id);
}
