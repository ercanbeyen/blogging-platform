package com.ercanbeyen.bloggingplatform.controller;

import com.ercanbeyen.bloggingplatform.dto.PostDto;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreatePostRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdatePostRequest;
import com.ercanbeyen.bloggingplatform.service.PostService;
import com.ercanbeyen.bloggingplatform.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Object> getPosts() {
        List<PostDto> postDtoList = postService.getPosts();

        postDtoList.forEach(postDto -> {
            Link postLink = WebMvcLinkBuilder.linkTo(PostController.class).slash(postDto.getId()).withSelfRel();
            postDto.add(postLink);
        });

        return ResponseEntity.ok(postDtoList);
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Object> getPost(@PathVariable("id") String id) {
        PostDto postDto = postService.getPost(id);
        List<Link> linkList = new ArrayList<>();

        Link postLink = WebMvcLinkBuilder.linkTo(PostController.class).slash(postDto.getId()).withSelfRel();
        linkList.add(postLink);

        String[] fields = {"comments", "likes", "dislikes"};

        for (String field : fields) {
            Link link = WebMvcLinkBuilder.linkTo(PostController.class).slash(postDto.getId()).slash(field).withRel(field);
            linkList.add(link);
        }

        postDto.add(linkList);

        return ResponseEntity.ok(postDto);
    }

    @PostMapping
    public ResponseEntity<Object> createPost(@RequestBody @Validated CreatePostRequest request) {
        SecurityUtil.checkBannedRole();
        return new ResponseEntity<>(postService.createPost(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePost(@PathVariable("id") String id, @RequestBody @Validated UpdatePostRequest request) {
        SecurityUtil.checkBannedRole();
        return ResponseEntity.ok(postService.updatePost(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePost(@PathVariable("id") String id) {
        SecurityUtil.checkAdminRole();
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

    @GetMapping(value = "/{id}/comments")
    public ResponseEntity<Object> getComments(@PathVariable("id") String id) {
        return ResponseEntity.ok(postService.getComments(id));
    }

}
