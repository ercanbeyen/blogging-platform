package com.ercanbeyen.bloggingplatform.controller;

import com.ercanbeyen.bloggingplatform.dto.CommentDto;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreateCommentRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateCommentRequest;
import com.ercanbeyen.bloggingplatform.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDto>> getComments() {
        return ResponseEntity.ok(commentService.getComments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getComment(@PathVariable("id") String id) {
        return ResponseEntity.ok(commentService.getComment(id));
    }

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody CreateCommentRequest request) {
        return new ResponseEntity<>(commentService.createComment(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("id") String id, UpdateCommentRequest request) {
        return ResponseEntity.ok(commentService.updateComment(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") String id) {
        return ResponseEntity.ok(commentService.deleteComment(id));
    }
}
