package com.ercanbeyen.bloggingplatform.controller;

import com.ercanbeyen.bloggingplatform.dto.request.create.CreateCommentRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateCommentRequest;
import com.ercanbeyen.bloggingplatform.service.CommentService;
import com.ercanbeyen.bloggingplatform.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<Object> getComments() {
        return ResponseEntity.ok(commentService.getComments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getComment(@PathVariable("id") String id) {
        return ResponseEntity.ok(commentService.getComment(id));
    }

    @PostMapping
    public ResponseEntity<Object> createComment(@RequestBody @Validated CreateCommentRequest request) {
        SecurityUtil.checkBannedRole();
        return new ResponseEntity<>(commentService.createComment(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateComment(@PathVariable("id") String id, @RequestBody @Validated UpdateCommentRequest request) {
        SecurityUtil.checkBannedRole();
        return ResponseEntity.ok(commentService.updateComment(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteComment(@PathVariable("id") String id, @RequestParam String postId) {
        SecurityUtil.checkAdminRole();
        return ResponseEntity.ok(commentService.deleteComment(id, postId));
    }
}
