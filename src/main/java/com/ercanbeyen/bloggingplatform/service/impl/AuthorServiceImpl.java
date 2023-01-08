package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.dto.request.create.CreateAuthorRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateAuthorRequest;
import com.ercanbeyen.bloggingplatform.exception.DocumentNotFound;
import com.ercanbeyen.bloggingplatform.dto.AuthorDto;
import com.ercanbeyen.bloggingplatform.dto.converter.AuthorDtoConverter;
import com.ercanbeyen.bloggingplatform.document.Author;
import com.ercanbeyen.bloggingplatform.repository.AuthorRepository;
import com.ercanbeyen.bloggingplatform.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorDtoConverter authorDtoConverter;

    @Override
    public AuthorDto createAuthor(CreateAuthorRequest request) {
        Author createdAuthor = Author.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .email(request.getEmail())
                .gender(request.getGender())
                .about(request.getAbout())
                .favoriteTopics(request.getFavoriteTopics())
                .location(request.getLocation())
                .createdAt(LocalDateTime.now())
                .build();

        return authorDtoConverter.convert(authorRepository.save(createdAuthor));
    }

    @Override
    public AuthorDto updateAuthor(String id, UpdateAuthorRequest request) {
        Author authorInDb = authorRepository.findById(id)
                        .orElseThrow(
                                () -> new DocumentNotFound("Author " + id + " is not found")
                        );

        authorInDb.setFirstName(request.getFirstName());
        authorInDb.setLastName(request.getLastName());
        authorInDb.setAbout(request.getAbout());
        authorInDb.setGender(request.getGender());
        authorInDb.setFavoriteTopics(request.getFavoriteTopics());
        authorRepository.save(authorInDb);


        return authorDtoConverter.convert(authorRepository.save(authorInDb));
    }

    @Override
    public AuthorDto getAuthor(String id) {
        Author authorInDb = authorRepository.findById(id)
                .orElseThrow(
                        () -> new DocumentNotFound("Author " + id + " is not found")
                );

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
}
