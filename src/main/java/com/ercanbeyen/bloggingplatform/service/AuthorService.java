package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.document.Author;
import com.ercanbeyen.bloggingplatform.dto.AuthorDto;
import com.ercanbeyen.bloggingplatform.dto.request.auth.RegistrationRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateAuthorDetailsRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateAuthorRolesRequest;

import java.util.List;

public interface AuthorService {
    Author createAuthor(RegistrationRequest request);
    AuthorDto updateAuthor(String id, UpdateAuthorDetailsRequest request);
    AuthorDto getAuthor(String id);
    List<AuthorDto> getAuthors();
    void deleteAuthor(String id);
    AuthorDto updateRolesOfAuthor(String id, UpdateAuthorRolesRequest request);
    Author getAuthorByUsername(String username);
    String followAuthor(String id, String authorId);
    String unFollowAuthor(String id, String authorId);
    List<String> getFollowedAuthors(String id);
    List<String> getFollowers(String id);
    Author getAuthorById(String id);
}
