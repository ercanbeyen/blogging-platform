package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.dto.AuthorDto;
import com.ercanbeyen.bloggingplatform.dto.RoleDto;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreateAuthorRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateAuthorRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateAuthorRolesRequest;

import java.util.List;

public interface AuthorService {
    AuthorDto createAuthor(CreateAuthorRequest request);
    AuthorDto updateAuthor(String id, UpdateAuthorRequest request);
    AuthorDto getAuthor(String id);
    List<AuthorDto> getAuthors();
    void deleteAuthor(String id);
    AuthorDto updateRolesOfAuthor(String id, UpdateAuthorRolesRequest request);
}
