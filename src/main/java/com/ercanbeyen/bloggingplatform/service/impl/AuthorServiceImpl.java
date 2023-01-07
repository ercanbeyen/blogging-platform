package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.exception.DocumentNotFound;
import com.ercanbeyen.bloggingplatform.dto.AuthorDto;
import com.ercanbeyen.bloggingplatform.dto.converter.AuthorDtoConverter;
import com.ercanbeyen.bloggingplatform.entity.Author;
import com.ercanbeyen.bloggingplatform.repository.AuthorRepository;
import com.ercanbeyen.bloggingplatform.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorDtoConverter authorDtoConverter;

    @Override
    public AuthorDto createAuthor(AuthorDto authorDto) {
        Author createdAuthor = Author.builder()
                .firstName(authorDto.getFirstName())
                .lastName(authorDto.getLastName())
                .username(authorDto.getUsername())
                .email(authorDto.getEmail())
                .gender(authorDto.getGender())
                .about(authorDto.getAbout())
                .favoriteTopics(authorDto.getFavoriteTopics())
                .location(authorDto.getLocation())
                .createdAt(LocalDateTime.now())
                .build();

        return authorDtoConverter.convert(authorRepository.save(createdAuthor));
    }

    @Override
    public AuthorDto updateAuthor(String id, AuthorDto authorDto) {
        Author authorInDb = authorRepository.findById(id)
                        .orElseThrow(
                                () -> new DocumentNotFound("Author " + id + " is not found")
                        );


        authorInDb.setFirstName(authorDto.getFirstName());
        authorInDb.setLastName(authorDto.getLastName());
        authorInDb.setAbout(authorDto.getAbout());
        authorInDb.setGender(authorDto.getGender());
        authorInDb.setFavoriteTopics(authorDto.getFavoriteTopics());
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
