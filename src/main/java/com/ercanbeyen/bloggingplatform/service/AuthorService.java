package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.document.Author;
import com.ercanbeyen.bloggingplatform.document.Response;
import com.ercanbeyen.bloggingplatform.dto.AuthorDto;
import com.ercanbeyen.bloggingplatform.dto.request.auth.RegistrationRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateAuthorDetailsRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateAuthorRolesRequest;

import java.util.List;

public interface AuthorService {
    Author createAuthor(RegistrationRequest request);
    Response<Object> updateAuthor(String id, UpdateAuthorDetailsRequest request);
    Response<Object> getAuthor(String id);
    Response<Object> getAuthors();
    void deleteAuthor(String id);
    Response<Object> updateRolesOfAuthor(String id, UpdateAuthorRolesRequest request);
    Author getAuthorByUsername(String username);
    Response<Object> followAuthor(String id, String authorId);
    Response<Object> unFollowAuthor(String id, String authorId);
    Response<Object> getFollowedAuthors(String id);
    Response<Object> getFollowers(String id);
}
