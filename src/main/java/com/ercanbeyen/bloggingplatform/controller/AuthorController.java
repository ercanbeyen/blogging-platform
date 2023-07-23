package com.ercanbeyen.bloggingplatform.controller;

import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateAuthorRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateAuthorRolesRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdatePasswordRequest;
import com.ercanbeyen.bloggingplatform.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<Object> getAuthors() {
        return ResponseEntity.ok(authorService.getAuthors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getAuthor(@PathVariable("id") String id) {
        return ResponseEntity.ok(authorService.getAuthor(id));
    }


    @PutMapping("/{id}/details")
    public ResponseEntity<Object> updateAuthor(@PathVariable("id") String id, @RequestBody @Validated UpdateAuthorRequest request) {
        return ResponseEntity.ok(authorService.updateAuthor(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable("id") String id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<Object> updateRolesOfAuthor(@PathVariable("id") String id, @RequestBody @Validated UpdateAuthorRolesRequest request) {
        return ResponseEntity.ok(authorService.updateRolesOfAuthor(id, request));
    }

    @PutMapping("/{id}/follow")
    public ResponseEntity<Object> followAuthor(@PathVariable("id") String id, @RequestParam String authorId) {
        return ResponseEntity.ok(authorService.followAuthor(id, authorId));
    }

    @PutMapping("/{id}/unfollow")
    public ResponseEntity<Object> unFollowAuthor(@PathVariable("id") String id, @RequestParam String authorId) {
        return ResponseEntity.ok(authorService.unFollowAuthor(id, authorId));
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<Object> getFollowers(@PathVariable("id") String id) {
        return ResponseEntity.ok(authorService.getFollowers(id));
    }

    @GetMapping("/{id}/followed")
    public ResponseEntity<Object> getFollowedAuthors(@PathVariable("id") String id) {
        return ResponseEntity.ok(authorService.getFollowedAuthors(id));
    }

    @GetMapping("/{id}/notifications")
    public ResponseEntity<Object> getNotifications(@PathVariable("id") String id) {
        return ResponseEntity.ok(authorService.getNotifications(id));
    }

    @PutMapping("/{id}/password-update")
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "id") String id, @RequestBody @Validated UpdatePasswordRequest request) {
        return ResponseEntity.ok(authorService.updatePassword(id, request));
    }
}
