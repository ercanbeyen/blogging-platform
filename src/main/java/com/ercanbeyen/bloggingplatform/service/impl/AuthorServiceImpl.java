package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.values.DocumentName;
import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import com.ercanbeyen.bloggingplatform.constant.enums.RoleName;
import com.ercanbeyen.bloggingplatform.document.Role;
import com.ercanbeyen.bloggingplatform.dto.AuthorDto;
import com.ercanbeyen.bloggingplatform.dto.NotificationDto;
import com.ercanbeyen.bloggingplatform.dto.request.auth.RegistrationRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateAuthorDetailsRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateAuthorRolesRequest;
import com.ercanbeyen.bloggingplatform.exception.data.DataConflict;
import com.ercanbeyen.bloggingplatform.exception.data.DataForbidden;
import com.ercanbeyen.bloggingplatform.exception.data.DataNotFound;
import com.ercanbeyen.bloggingplatform.dto.converter.AuthorDtoConverter;
import com.ercanbeyen.bloggingplatform.document.Author;
import com.ercanbeyen.bloggingplatform.repository.AuthorRepository;
import com.ercanbeyen.bloggingplatform.service.AuthorService;
import com.ercanbeyen.bloggingplatform.service.NotificationService;
import com.ercanbeyen.bloggingplatform.service.RoleService;
import com.ercanbeyen.bloggingplatform.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorDtoConverter authorDtoConverter;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final NotificationService notificationService;

    @Transactional
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

    @Transactional
    @Override
    public AuthorDto updateAuthor(String id, UpdateAuthorDetailsRequest request) {
        Author loggedIn_author = SecurityUtil.getLoggedInAuthor();
        String loggedIn_authorId = loggedIn_author.getId();

        if (!loggedIn_authorId.equals(id)) {
            throw new DataForbidden(ResponseMessage.NOT_AUTHORIZED);
        }

        Author authorInDb = findAuthorById(id);

        authorInDb.setFirstName(request.getFirstName());
        authorInDb.setLastName(request.getLastName());
        authorInDb.setAbout(request.getAbout());
        authorInDb.setGender(request.getGender());
        authorInDb.setFavoriteTopics(request.getFavoriteTopics());
        authorInDb.setLocation(request.getLocation());

        Author updatedAuthor = authorRepository.save(authorInDb);

        return authorDtoConverter.convert(updatedAuthor);
    }

    @Override
    public AuthorDto getAuthor(String id) {
        Author authorInDb = findAuthorById(id);
        authorInDb.getRoles().forEach(System.out::println);

        return authorDtoConverter.convert(authorInDb);
    }

    @Override
    public List<AuthorDto> getAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(authorDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteAuthor(String id) {
        boolean doesExist = authorRepository.findAll()
                .stream()
                .anyMatch(author -> author.getId().equals(id));

        if (!doesExist) {
            throw new DataNotFound(String.format(ResponseMessage.NOT_FOUND, DocumentName.AUTHOR, id));
        }

        authorRepository.deleteById(id);
    }

    @Transactional
    @Override
    public AuthorDto updateRolesOfAuthor(String id, UpdateAuthorRolesRequest request) {
        Author authorInDb = findAuthorById(id);
        Set<Role> roles = new HashSet<>();
        Set<RoleName> roleNames = request.getRoles();

        if (roleNames.isEmpty() || !roleNames.contains(RoleName.USER)) {
            throw new DataConflict("Roles set is invalid");
        }

        request.getRoles().forEach(
                roleName -> {
                    Role roleInDb = roleService.getRoleByRoleName(roleName);
                    roles.add(roleInDb);
                }
        );

        authorInDb.setRoles(roles);
        Author updatedAuthor = authorRepository.save(authorInDb);

        return authorDtoConverter.convert(updatedAuthor);
    }

    @Override
    public Author getAuthorByUsername(String username) {
        return authorRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, DocumentName.AUTHOR, username)));
    }

    @Transactional
    @Override
    public String followAuthor(String id, String authorId) {
        Author loggedInAuthor = SecurityUtil.getLoggedInAuthor();
        Author follower = findAuthorById(id);

        if (!follower.getId().equals(loggedInAuthor.getId())) {
            throw new DataConflict(ResponseMessage.NOT_AUTHORIZED);
        }

        Author unfollowed = authorRepository.findById(authorId)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, DocumentName.AUTHOR, authorId)));

        if (follower.getId().equals(unfollowed.getId())) {
            throw new DataConflict("You cannot follow yourself");
        }

        boolean isFollowed = follower.getFollowed()
                .stream()
                .anyMatch(followed -> followed.getId().equals(unfollowed.getId()));

        if (isFollowed) {
            throw new DataConflict("You are already following Author " + authorId);
        }

        follower.getFollowed().add(unfollowed);
        unfollowed.getFollowers().add(follower);

        authorRepository.save(follower);
        authorRepository.save(unfollowed);

        return DocumentName.AUTHOR + " " + authorId + " is added to your followed authors";
    }

    @Transactional
    @Override
    public String unFollowAuthor(String id, String authorId) {
        Author loggedInAuthor = SecurityUtil.getLoggedInAuthor();
        Author follower = findAuthorById(id);

        if (!follower.getId().equals(loggedInAuthor.getId())) {
            throw new DataConflict(ResponseMessage.NOT_AUTHORIZED);
        }

        Author followed = authorRepository.findById(authorId)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, DocumentName.AUTHOR, authorId)));

        if (follower.getId().equals(followed.getId())) {
            throw new DataConflict("You cannot unfollow yourself");
        }

        boolean isFollowed = follower.getFollowed()
                .stream()
                .anyMatch(followedAuthor -> followedAuthor.getId().equals(authorId));

        if (!isFollowed) {
            throw new DataConflict("You are already not following Author " + authorId);
        }

        follower.getFollowed().remove(followed);
        followed.getFollowers().remove(follower);

        authorRepository.save(follower);
        authorRepository.save(followed);

        return DocumentName.AUTHOR + " " + authorId + " is removed from your followed authors";
    }

    @Override
    public List<String> getFollowedAuthors(String id) {
        Author follower = findAuthorById(id);

        return follower.getFollowed()
                .stream()
                .map(Author::getId)
                .toList();
    }

    @Override
    public List<String> getFollowers(String id) {
        Author followed = findAuthorById(id);

        return followed.getFollowers()
                .stream()
                .map(Author::getId)
                .toList();
    }

    @Override
    public List<NotificationDto> getNotifications(String toAuthorId) {
        Author loggedInAuthor = SecurityUtil.getLoggedInAuthor();
        String loggedInAuthorId = loggedInAuthor.getId();

        if (!loggedInAuthorId.equals(toAuthorId)) {
            throw new DataForbidden(ResponseMessage.NOT_AUTHORIZED);
        }

        boolean isAuthorInDb = authorRepository.findAll()
                .stream()
                .anyMatch(author -> author.getId().equals(toAuthorId));

        if (!isAuthorInDb) {
            throw new DataNotFound(String.format(ResponseMessage.NOT_FOUND, DocumentName.AUTHOR, toAuthorId));
        }

        return notificationService.getNotifications(null, toAuthorId);
    }

    @Override
    public void enableAuthor(String authorId) {
        Author authorInDb = authorRepository.findById(authorId)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, DocumentName.AUTHOR, authorId)));

        boolean isEnabled = true;

        authorInDb.setEnabled(isEnabled);
        authorRepository.save(authorInDb);
    }

    private Author findAuthorById(String id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, DocumentName.AUTHOR, id)));
    }

    @Override
    public boolean doesAuthorExist(String username) {
        return authorRepository.findAll()
                .stream()
                .anyMatch(author -> author.getUsername().equals(username));
    }

}
