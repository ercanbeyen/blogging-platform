package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.messages.AuthMessage;
import com.ercanbeyen.bloggingplatform.constant.RoleName;
import com.ercanbeyen.bloggingplatform.constant.messages.ExceptionMessage;
import com.ercanbeyen.bloggingplatform.document.Response;
import com.ercanbeyen.bloggingplatform.document.Role;
import com.ercanbeyen.bloggingplatform.dto.request.auth.RegistrationRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateAuthorDetailsRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateAuthorRolesRequest;
import com.ercanbeyen.bloggingplatform.exception.DocumentConflict;
import com.ercanbeyen.bloggingplatform.exception.DocumentForbidden;
import com.ercanbeyen.bloggingplatform.exception.DocumentNotFound;
import com.ercanbeyen.bloggingplatform.dto.converter.AuthorDtoConverter;
import com.ercanbeyen.bloggingplatform.document.Author;
import com.ercanbeyen.bloggingplatform.repository.AuthorRepository;
import com.ercanbeyen.bloggingplatform.service.AuthorService;
import com.ercanbeyen.bloggingplatform.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorDtoConverter authorDtoConverter;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    public Author createAuthor(RegistrationRequest request) {
        Role role = roleService.getRoleByRoleName(RoleName.USER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        Author newAuthor = Author.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .roles(roles)
                .email(request.getEmail())
                .createdAt(LocalDateTime.now())
                .followed(new ArrayList<>())
                .followers(new ArrayList<>())
                .build();

        return authorRepository.save(newAuthor);
    }

    @Override
    public Response<Object> updateAuthor(String id, UpdateAuthorDetailsRequest request) {
        Author loggedIn_author = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedIn_authorId = loggedIn_author.getId();

        if (!loggedIn_authorId.equals(id)) {
            throw new DocumentForbidden(AuthMessage.NOT_AUTHORIZED);
        }

        Author authorInDb = authorRepository
                .findById(id)
                .orElseThrow(() -> new DocumentNotFound(String.format(ExceptionMessage.NOT_FOUND, "Author", id)));

        authorInDb.setFirstName(request.getFirstName());
        authorInDb.setLastName(request.getLastName());
        authorInDb.setAbout(request.getAbout());
        authorInDb.setGender(request.getGender());
        authorInDb.setFavoriteTopics(request.getFavoriteTopics());
        authorInDb.setLocation(request.getLocation());

        Author updatedAuthor = authorRepository.save(authorInDb);

        return Response.builder()
                .success(true)
                .message(AuthMessage.SUCCESS)
                .data(authorDtoConverter.convert(updatedAuthor))
                .build();
    }

    @Override
    public Response<Object> getAuthor(String id) {
        Author authorInDb = authorRepository
                .findById(id)
                .orElseThrow(() -> new DocumentNotFound(String.format(ExceptionMessage.NOT_FOUND, "Author", id)));

        authorInDb.getRoles().forEach(System.out::println);

        return Response.builder()
                .success(true)
                .message(AuthMessage.SUCCESS)
                .data(authorDtoConverter.convert(authorInDb))
                .build();
    }

    @Override
    public Response<Object> getAuthors() {
        List<Author> authors = authorRepository.findAll();
        return Response.builder()
                .success(true)
                .message(AuthMessage.SUCCESS)
                .data(authors.stream()
                        .map(authorDtoConverter::convert)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public void deleteAuthor(String id) {
        authorRepository.deleteById(id);
    }

    @Override
    public Response<Object> updateRolesOfAuthor(String id, UpdateAuthorRolesRequest request) {
        Author authorInDb = authorRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFound(String.format(ExceptionMessage.NOT_FOUND, "Author", id)));

        Set<Role> roles = new HashSet<>();
        Set<RoleName> roleNames = request.getRoles();

        if (roleNames.isEmpty() || !roleNames.contains(RoleName.USER)) {
            throw new DocumentConflict("Roles set is invalid");
        }

        request.getRoles().forEach(
                roleName -> {
                    Role roleInDb = roleService.getRoleByRoleName(roleName);
                    roles.add(roleInDb);
                }
        );

        authorInDb.setRoles(roles);
        Author updatedAuthor = authorRepository.save(authorInDb);

        return Response.builder()
                .success(true)
                .message(AuthMessage.SUCCESS)
                .data(authorDtoConverter.convert(updatedAuthor))
                .build();
    }

    @Override
    public Author getAuthorByUsername(String username) {
        return authorRepository
                .findByUsername(username)
                .orElseThrow(() -> new DocumentNotFound(String.format(ExceptionMessage.NOT_FOUND, "Author", username)));
    }

    @Transactional
    @Override
    public Response<Object> followAuthor(String id, String authorId) {
        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Author follower = authorRepository
                .findById(id)
                .orElseThrow(() -> new DocumentNotFound(String.format(ExceptionMessage.NOT_FOUND, "Author", id)));

        if (!follower.getId().equals(loggedInAuthor.getId())) {
            throw new DocumentConflict(AuthMessage.NOT_AUTHORIZED);
        }

        Author unfollowed = authorRepository
                .findById(authorId)
                .orElseThrow(() -> new DocumentNotFound(String.format(ExceptionMessage.NOT_FOUND, "Author", authorId)));

        if (follower.getId().equals(unfollowed.getId())) {
            throw new DocumentConflict("You cannot follow yourself");
        }

        boolean isFollowed = follower
                .getFollowed()
                .stream()
                .anyMatch(followed -> followed.getId().equals(unfollowed.getId()));

        if (isFollowed) {
            throw new DocumentConflict("You are already following Author " + authorId);
        }

        follower.getFollowed().add(unfollowed);
        unfollowed.getFollowers().add(follower);

        authorRepository.save(follower);
        authorRepository.save(unfollowed);

        String message = "Author " + authorId + " is added to your followed authors";

        return Response.builder()
                .success(true)
                .message(AuthMessage.SUCCESS)
                .data(message)
                .build();
    }

    @Transactional
    @Override
    public Response<Object> unFollowAuthor(String id, String authorId) {
        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Author follower = authorRepository
                .findById(id)
                .orElseThrow(() -> new DocumentNotFound(String.format(ExceptionMessage.NOT_FOUND, "Author", id)));

        if (!follower.getId().equals(loggedInAuthor.getId())) {
            throw new DocumentConflict(AuthMessage.NOT_AUTHORIZED);
        }

        Author followed = authorRepository
                .findById(authorId)
                .orElseThrow(() -> new DocumentNotFound(String.format(ExceptionMessage.NOT_FOUND, "Author", authorId)));

        if (follower.getId().equals(followed.getId())) {
            throw new DocumentConflict("You cannot unfollow yourself");
        }

        boolean isFollowed = follower
                .getFollowed()
                .stream()
                .anyMatch(followedAuthor -> followedAuthor.getId().equals(authorId));

        if (!isFollowed) {
            throw new DocumentConflict("You are already not following Author " + authorId);
        }

        follower.getFollowed().remove(followed);
        followed.getFollowers().remove(follower);

        authorRepository.save(follower);
        authorRepository.save(followed);

        String message = "Author " + authorId + " is removed from your followed authors";

        return Response.builder()
                .success(true)
                .message(AuthMessage.SUCCESS)
                .data(message)
                .build();
    }

    @Override
    public Response<Object> getFollowedAuthors(String id) {
        Author follower = authorRepository
                .findById(id)
                .orElseThrow(() -> new DocumentNotFound(String.format(ExceptionMessage.NOT_FOUND, "Author", id)));

        List<String> authors = follower
                .getFollowed()
                .stream()
                .map(Author::getId)
                .toList();

        return Response.builder()
                .success(true)
                .message(AuthMessage.SUCCESS)
                .data(authors)
                .build();
    }

    @Override
    public Response<Object> getFollowers(String id) {
        Author followed = authorRepository
                .findById(id)
                .orElseThrow(() -> new DocumentNotFound(String.format(ExceptionMessage.NOT_FOUND, "Author", id)));

        List<String> authors = followed
                .getFollowers()
                .stream()
                .map(Author::getId)
                .toList();

        return Response.builder()
                .success(true)
                .message(AuthMessage.SUCCESS)
                .data(authors)
                .build();
    }

}
