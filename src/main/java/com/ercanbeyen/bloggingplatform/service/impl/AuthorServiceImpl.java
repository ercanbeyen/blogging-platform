package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.values.EntityName;
import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import com.ercanbeyen.bloggingplatform.constant.enums.RoleName;
import com.ercanbeyen.bloggingplatform.entity.Role;
import com.ercanbeyen.bloggingplatform.dto.AuthorDto;
import com.ercanbeyen.bloggingplatform.dto.NotificationDto;
import com.ercanbeyen.bloggingplatform.dto.request.auth.RegistrationRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateAuthorRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateAuthorRolesRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdatePasswordRequest;
import com.ercanbeyen.bloggingplatform.exception.data.DataConflict;
import com.ercanbeyen.bloggingplatform.exception.data.DataNotFound;
import com.ercanbeyen.bloggingplatform.dto.converter.AuthorDtoConverter;
import com.ercanbeyen.bloggingplatform.entity.Author;
import com.ercanbeyen.bloggingplatform.repository.AuthorRepository;
import com.ercanbeyen.bloggingplatform.service.AuthorService;
import com.ercanbeyen.bloggingplatform.service.NotificationService;
import com.ercanbeyen.bloggingplatform.service.RoleService;
import com.ercanbeyen.bloggingplatform.util.SecurityUtil;
import com.ercanbeyen.bloggingplatform.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
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
                .password(encodePassword(request.getPassword()))
                .username(request.getUsername())
                .roles(roles)
                .email(request.getEmail())
                .createdAt(TimeUtil.calculateNow())
                .followed(new ArrayList<>())
                .followers(new ArrayList<>())
                .build();

        return authorRepository.save(newAuthor);
    }

    @Transactional
    @Override
    public AuthorDto updateAuthor(String id, UpdateAuthorRequest request) {
        Author loggedInAuthor = SecurityUtil.getLoggedInAuthor();
        Optional<Author> optionalAuthor = authorRepository.findByUsername(request.getUsername());

        if (optionalAuthor.isPresent() && !optionalAuthor.get().getId().equals(id)) {
            throw new DataConflict("Username is already in use");
        }

        loggedInAuthor.setUsername(request.getUsername());
        loggedInAuthor.setEmail(request.getEmail());
        loggedInAuthor.setFirstName(request.getFirstName());
        loggedInAuthor.setLastName(request.getLastName());
        loggedInAuthor.setAbout(request.getAbout());
        loggedInAuthor.setGender(request.getGender());
        loggedInAuthor.setFavoriteTopics(request.getFavoriteTopics());
        loggedInAuthor.setLocation(request.getLocation());

        Author updatedAuthor = authorRepository.save(loggedInAuthor);

        return authorDtoConverter.convert(updatedAuthor);
    }

    @Override
    public AuthorDto getAuthor(String id) {
        Author authorInDb = findAuthorById(id);
        authorInDb.getRoles().forEach(role -> log.info("Role: {}", role));

        return authorDtoConverter.convert(authorInDb);
    }

    @Override
    public List<AuthorDto> getAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(authorDtoConverter::convert)
                .toList();
    }

    @Transactional
    @Override
    public void deleteAuthor(String id) {
        boolean doesExist = authorRepository.findAll()
                .stream()
                .anyMatch(author -> author.getId().equals(id));

        if (!doesExist) {
            throw new DataNotFound(String.format(ResponseMessage.NOT_FOUND, EntityName.AUTHOR, id));
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
    public Author findAuthorByUsername(String username) {
        return authorRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, EntityName.AUTHOR, username)));
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
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, EntityName.AUTHOR, authorId)));

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

        return EntityName.AUTHOR + " " + authorId + " is added to your followed authors";
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
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, EntityName.AUTHOR, authorId)));

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

        return EntityName.AUTHOR + " " + authorId + " is removed from your followed authors";
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
        boolean isAuthorInDb = authorRepository.findAll()
                .stream()
                .anyMatch(author -> author.getId().equals(toAuthorId));

        if (!isAuthorInDb) {
            throw new DataNotFound(String.format(ResponseMessage.NOT_FOUND, EntityName.AUTHOR, toAuthorId));
        }

        return notificationService.getNotifications(null, toAuthorId);
    }

    @Override
    public void enableAuthor(String authorId) {
        Author authorInDb = authorRepository.findById(authorId)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, EntityName.AUTHOR, authorId)));

        authorInDb.setEnabled(true);
        authorRepository.save(authorInDb);
    }

    @Override
    public boolean authorExistsById(String id) {
        return authorRepository.findById(id)
                .isPresent();
    }

    @Override
    public boolean authorExistsByUsername(String username) {
        return authorRepository.findByUsername(username)
                .isPresent();
    }

    @Override
    public String updatePassword(String id, UpdatePasswordRequest request) {
        Author loggedInAuthor = SecurityUtil.getLoggedInAuthor();

        if (!request.getPassword().equals(request.getValidationPassword())) {
            throw new DataConflict("Password mismatch");
        }

        updatePassword(loggedInAuthor.getUsername(), request.getPassword());

        return "Password is successfully updated";
    }

    @Override
    public void updatePassword(String username, String password) {
        Author authorInDb = findAuthorByUsername(username);
        authorInDb.setPassword(encodePassword(password));
        authorRepository.save(authorInDb);
    }

    private Author findAuthorById(String id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, EntityName.AUTHOR, id)));
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
