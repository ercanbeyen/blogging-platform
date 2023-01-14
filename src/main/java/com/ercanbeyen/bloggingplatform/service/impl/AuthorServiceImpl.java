package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.RoleName;
import com.ercanbeyen.bloggingplatform.document.Role;
import com.ercanbeyen.bloggingplatform.dto.request.auth.RegistrationRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateAuthorDetailsRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateAuthorRolesRequest;
import com.ercanbeyen.bloggingplatform.exception.DocumentConflict;
import com.ercanbeyen.bloggingplatform.exception.DocumentForbidden;
import com.ercanbeyen.bloggingplatform.exception.DocumentNotFound;
import com.ercanbeyen.bloggingplatform.dto.AuthorDto;
import com.ercanbeyen.bloggingplatform.dto.converter.AuthorDtoConverter;
import com.ercanbeyen.bloggingplatform.document.Author;
import com.ercanbeyen.bloggingplatform.repository.AuthorRepository;
import com.ercanbeyen.bloggingplatform.service.AuthorService;
import com.ercanbeyen.bloggingplatform.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        HashSet<Role> roles = new HashSet<>();
        roles.add(role);

        Author newAuthor = Author.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .roles(roles)
                .email(request.getEmail())
                .createdAt(LocalDateTime.now())
                .build();

        return authorRepository.save(newAuthor);
    }



    @Override
    public AuthorDto updateAuthor(String id, UpdateAuthorDetailsRequest request) {
        Author loggedIn_author = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedIn_authorId = loggedIn_author.getId();

        if (!loggedIn_authorId.equals(id)) {
            throw new DocumentForbidden("You are not authorized");
        }

        Author authorInDb = authorRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFound("Author " + id + " is not found"));

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
        Author authorInDb = authorRepository.findById(id)
                .orElseThrow(
                        () -> new DocumentNotFound("Author " + id + " is not found"));

        authorInDb.getRoles().forEach(System.out::println);

        return authorDtoConverter.convert(authorInDb);
    }

    @Override
    public List<AuthorDto> getAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream()
                .map(authorDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAuthor(String id) {
        authorRepository.deleteById(id);
    }

    @Override
    public AuthorDto updateRolesOfAuthor(String id, UpdateAuthorRolesRequest request) {
        Author authorInDb = authorRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFound("Author " + id + " is not found"));

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

        return authorDtoConverter.convert(updatedAuthor);
    }

    @Override
    public Author getAuthorByUsername(String username) {
        return authorRepository.findByUsername(username)
                .orElseThrow(() -> new DocumentNotFound("Author " + username + " is not found"));
    }
}
