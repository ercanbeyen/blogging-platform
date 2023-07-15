package com.ercanbeyen.bloggingplatform.controller;

import com.ercanbeyen.bloggingplatform.dto.AuthorDto;
import com.ercanbeyen.bloggingplatform.dto.PostDto;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreatePostRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdatePostRequest;
import com.ercanbeyen.bloggingplatform.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<Object> getPosts() {
        return ResponseEntity.ok(postService.getPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getPost(@PathVariable("id") String id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    @PostMapping
    public ResponseEntity<Object> createPost(@RequestBody @Validated CreatePostRequest request) {
        return new ResponseEntity<>(postService.createPost(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePost(@PathVariable("id") String id, @RequestBody @Validated UpdatePostRequest request) {
        return ResponseEntity.ok(postService.updatePost(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePost(@PathVariable("id") String id) {
        return ResponseEntity.ok(postService.deletePost(id));
    }

    @PutMapping("/{id}/status/like")
    public ResponseEntity<Object> likePost(@PathVariable("id") String id) {
        return ResponseEntity.ok(postService.likePost(id));
    }

    @PutMapping("/{id}/status/dislike")
    public ResponseEntity<Object> dislikePost(@PathVariable("id") String id) {
        return ResponseEntity.ok(postService.dislikePost(id));
    }

    @PutMapping("/{id}/status/remove")
    public ResponseEntity<Object> removeStatus(@PathVariable("id") String id) {
        return ResponseEntity.ok(postService.removeStatus(id));
    }

    @GetMapping("/{id}/likes")
    public ResponseEntity<Object> getAuthorsLiked(@PathVariable("id") String id) {
        return ResponseEntity.ok(postService.getAuthorsLiked(id));
    }

    @GetMapping("/{id}/dislikes")
    public ResponseEntity<Object> getAuthorsDisliked(@PathVariable("id") String id) {
        return ResponseEntity.ok(postService.getAuthorsDisliked(id));
    }

}
