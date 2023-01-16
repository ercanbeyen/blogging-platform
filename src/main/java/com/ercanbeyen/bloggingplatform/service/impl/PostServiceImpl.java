package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.RoleName;
import com.ercanbeyen.bloggingplatform.document.Author;
import com.ercanbeyen.bloggingplatform.document.Comment;
import com.ercanbeyen.bloggingplatform.document.Role;
import com.ercanbeyen.bloggingplatform.dto.AuthorDto;
import com.ercanbeyen.bloggingplatform.dto.PostDto;
import com.ercanbeyen.bloggingplatform.dto.converter.AuthorDtoConverter;
import com.ercanbeyen.bloggingplatform.dto.converter.PostDtoConverter;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreatePostRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdatePostRequest;
import com.ercanbeyen.bloggingplatform.document.Post;
import com.ercanbeyen.bloggingplatform.exception.DocumentConflict;
import com.ercanbeyen.bloggingplatform.exception.DocumentForbidden;
import com.ercanbeyen.bloggingplatform.exception.DocumentNotFound;
import com.ercanbeyen.bloggingplatform.repository.PostRepository;
import com.ercanbeyen.bloggingplatform.service.PostService;
import lombok.RequiredArgsConstructor;
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
    private final AuthorDtoConverter authorDtoConverter;

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

    @Override
    public String likePost(String id) {
        Post postInDb = postRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFound("Post " + id + " is not found"));

        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean isLiked = postInDb.getAuthorsLiked().stream()
                .anyMatch(author -> author.getId().equals(loggedInAuthor.getId()));

        if (isLiked) {
            throw new DocumentConflict("You have already liked the post");
        }

        postInDb.getAuthorsDisliked().remove(loggedInAuthor);
        postInDb.getAuthorsLiked().add(loggedInAuthor);
        postRepository.save(postInDb);

        return "You liked post " + postInDb.getId();
    }

    @Override
    public String dislikePost(String id) {
        Post postInDb = postRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFound("Post " + id + " is not found"));

        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean isDisliked = postInDb.getAuthorsDisliked().stream()
                .anyMatch(author -> author.getId().equals(loggedInAuthor.getId()));

        if (isDisliked) {
            throw new DocumentConflict("You have already disliked the post");
        }

        postInDb.getAuthorsLiked().remove(loggedInAuthor);
        postInDb.getAuthorsDisliked().add(loggedInAuthor);
        postRepository.save(postInDb);

        return "You disliked post " + postInDb.getId();
    }

    @Override
    public String removeStatus(String id) {
        Post postInDb = postRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFound("Post " + id + " is not found"));

        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean isLiked = postInDb.getAuthorsLiked().stream()
                .anyMatch(author -> author.getId().equals(loggedInAuthor.getId()));

        boolean isDisliked = postInDb.getAuthorsDisliked().stream()
                .anyMatch(author -> author.getId().equals(loggedInAuthor.getId()));

        if (!isLiked && !isDisliked) {
            throw new DocumentConflict("You have neither liked nor disliked");
        }

        String status;

        if (isLiked) {
            postInDb.getAuthorsLiked().remove(loggedInAuthor);
            status = "like";
        } else {
            postInDb.getAuthorsDisliked().remove(loggedInAuthor);
            status = "dislike";
        }

        postRepository.save(postInDb);

        return "Your " + status + " for post " + postInDb.getId() + " is removed";
    }

    @Override
    public List<AuthorDto> getAuthorsLiked(String id) {
        Post postInDb = postRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFound("Post " + id + " is not found"));

        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!postInDb.getAuthor().getId().equals(loggedInAuthor.getId())) {
            throw new DocumentForbidden("You are not authorized");
        }

        List<Author> authors = postInDb.getAuthorsLiked();

        return authors.stream()
                .map(authorDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuthorDto> getAuthorsDisliked(String id) {
        Post postInDb = postRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFound("Post " + id + " is not found"));

        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!postInDb.getAuthor().getId().equals(loggedInAuthor.getId())) {
            throw new DocumentForbidden("You are not authorized");
        }

        List<Author> authors = postInDb.getAuthorsDisliked();

        return authors.stream()
                .map(authorDtoConverter::convert)
                .collect(Collectors.toList());
    }

}
