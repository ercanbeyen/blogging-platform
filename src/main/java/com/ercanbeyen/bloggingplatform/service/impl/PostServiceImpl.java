package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.RoleName;
import com.ercanbeyen.bloggingplatform.document.Author;
import com.ercanbeyen.bloggingplatform.document.Comment;
import com.ercanbeyen.bloggingplatform.document.Role;
import com.ercanbeyen.bloggingplatform.dto.PostDto;
import com.ercanbeyen.bloggingplatform.dto.converter.PostDtoConverter;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreatePostRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdatePostRequest;
import com.ercanbeyen.bloggingplatform.document.Post;
import com.ercanbeyen.bloggingplatform.exception.DocumentForbidden;
import com.ercanbeyen.bloggingplatform.exception.DocumentNotFound;
import com.ercanbeyen.bloggingplatform.repository.PostRepository;
import com.ercanbeyen.bloggingplatform.service.AuthorService;
import com.ercanbeyen.bloggingplatform.service.PostService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostDtoConverter postDtoConverter;

    @Override
    public PostDto createPost(CreatePostRequest request) {
        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Post createdPost = Post.builder()
                .author(loggedInAuthor)
                .title(request.getTitle())
                .text(request.getText())
                .category(request.getCategory())
                .tags(request.getTags())
                .comments(new ArrayList<>())
                .latestChangeAt(LocalDateTime.now())
                .build();

        return postDtoConverter.convert(postRepository.save(createdPost));
    }

    @Override
    public PostDto updatePost(String id, UpdatePostRequest request) {
        Post postInDb = postRepository.findById(id)
                .orElseThrow(
                        () -> new DocumentNotFound("Post " + id + " is not found")
                );

        Author author_posted = postInDb.getAuthor();
        Author loggedIn_author = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!author_posted.getId().equals(loggedIn_author.getId())) {
            throw new DocumentForbidden("You are not authorized");
        }

        postInDb.setTitle(request.getTitle());
        postInDb.setText(request.getText());
        postInDb.setCategory(request.getCategory());
        postInDb.setTags(request.getTags());
        postInDb.setLatestChangeAt(LocalDateTime.now());

        return postDtoConverter.convert(postRepository.save(postInDb));
    }

    @Override
    public List<PostDto> getPosts() {
        List<Post> posts = postRepository.findAll();

        return posts.stream()
                .map(postDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public PostDto getPost(String id) {
        Post postInDb = postRepository.findById(id)
                .orElseThrow(
                        () -> new DocumentNotFound("Post " + id + " is not found")
                );

        return postDtoConverter.convert(postInDb);
    }

    @Override
    public String deletePost(String id) {
        Post postInDb = postRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFound("Post " + id + " is not found"));

        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<Role> loggedInAuthorRoles = loggedInAuthor.getRoles();
        Set<RoleName> roles = loggedInAuthorRoles.stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        if (!roles.contains(RoleName.ADMIN) && postInDb.getAuthor().getId().equals(loggedInAuthor.getId())) {
            throw new DocumentForbidden("You are not authorized");
        }
        postRepository.deleteById(id);
        return "Post " + id + " is successfully deleted";
    }

    @Override
    public void addCommentToPost(String id, Comment comment) {
        Post postInDb = postRepository.findById(id)
                .orElseThrow(
                        () -> new DocumentNotFound("Post " + id + " is not found")
                );

        postInDb.getComments().add(comment);
        postRepository.save(postInDb);
    }

    @Override
    public void deleteCommentFromPost(String postId, String commentId) {
        Post postInDb = postRepository.findById(postId)
                .orElseThrow(
                        () -> new DocumentNotFound("Post " + postId + " is not found")
                );

        Comment commentInPost = postInDb.getComments().stream()
                .filter(comment -> comment.getId().equals(commentId))
                .findAny()
                .orElseThrow(
                        () -> new DocumentNotFound("Comment " + commentId + " is not found inside Post " + postId)
                );

        postInDb.getComments().remove(commentInPost);
        postRepository.save(postInDb);
    }

}
