package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.entity.Author;
import com.ercanbeyen.bloggingplatform.dto.AuthorDto;
import com.ercanbeyen.bloggingplatform.dto.NotificationDto;
import com.ercanbeyen.bloggingplatform.dto.request.auth.RegistrationRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateAuthorRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateAuthorRolesRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdatePasswordRequest;

import java.util.List;

public interface AuthorService {
    Author createAuthor(RegistrationRequest request);
    AuthorDto updateAuthor(String id, UpdateAuthorRequest request);
    AuthorDto getAuthor(String id);
    List<AuthorDto> getAuthors();
    void deleteAuthor(String id);
    AuthorDto updateRolesOfAuthor(String id, UpdateAuthorRolesRequest request);
    Author findAuthorByUsername(String username);
    String followAuthor(String id, String authorId);
    String unFollowAuthor(String id, String authorId);
    List<String> getFollowedAuthors(String id);
    List<String> getFollowers(String id);
    List<NotificationDto> getNotifications(String toAuthorId);
    void enableAuthor(String authorId);
    boolean authorExistsById(String id);
    boolean authorExistsByUsername(String username);
    String updatePassword(String id, UpdatePasswordRequest request);
    void updatePassword(String username, String password);
}
