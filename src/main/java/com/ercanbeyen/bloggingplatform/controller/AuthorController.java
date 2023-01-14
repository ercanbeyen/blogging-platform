package com.ercanbeyen.bloggingplatform.controller;

import com.ercanbeyen.bloggingplatform.dto.AuthorDto;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateAuthorDetailsRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateAuthorRolesRequest;
import com.ercanbeyen.bloggingplatform.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<AuthorDto>> getAuthors() {
        return ResponseEntity.ok(authorService.getAuthors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable("id") String id) {
        return ResponseEntity.ok(authorService.getAuthor(id));
    }


    @PutMapping("/{id}/details")
    public ResponseEntity<AuthorDto> updateAuthorDetails(@PathVariable("id") String id, @RequestBody UpdateAuthorDetailsRequest request) {
        return ResponseEntity.ok(authorService.updateAuthor(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable("id") String id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<AuthorDto> updateRolesOfAuthor(@PathVariable("id") String id, @RequestBody UpdateAuthorRolesRequest request) {
        return ResponseEntity.ok(authorService.updateRolesOfAuthor(id, request));
    }
}
