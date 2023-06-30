package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.messages.AuthMessage;
import com.ercanbeyen.bloggingplatform.constant.RoleName;
import com.ercanbeyen.bloggingplatform.constant.messages.ExceptionMessage;
import com.ercanbeyen.bloggingplatform.constant.messages.NotificationMessage;
import com.ercanbeyen.bloggingplatform.document.*;
import com.ercanbeyen.bloggingplatform.dto.PostDto;
import com.ercanbeyen.bloggingplatform.dto.converter.AuthorDtoConverter;
import com.ercanbeyen.bloggingplatform.dto.converter.PostDtoConverter;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreatePostRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdatePostRequest;
import com.ercanbeyen.bloggingplatform.exception.DocumentConflict;
import com.ercanbeyen.bloggingplatform.exception.DocumentForbidden;
import com.ercanbeyen.bloggingplatform.exception.DocumentNotFound;
import com.ercanbeyen.bloggingplatform.repository.PostRepository;
import com.ercanbeyen.bloggingplatform.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public Response<Object> createPost(CreatePostRequest request) {
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

        Post savedPost = postRepository.save(createdPost);

        String senderMessage = "Dear " + loggedInAuthor.getUsername() + ", you posted a new article called.";
        kafkaTemplate.send(NotificationMessage.POST_NOTIFICATION, senderMessage);

        PostDto postDto = postDtoConverter.convert(savedPost);
        List<Author> followers = loggedInAuthor.getFollowers();

        for (Author follower : followers) {
            String receiverMessage = "Dear " + follower.getUsername() + ", author " + loggedInAuthor.getUsername() + " posted a new article.";
            kafkaTemplate.send(NotificationMessage.POST_NOTIFICATION, receiverMessage);
        }

        return Response.builder()
                .success(true)
                .message(AuthMessage.SUCCESS)
                .data(postDto)
                .build();
    }

    @Override
    public Response<Object> updatePost(String id, UpdatePostRequest request) {
        Post postInDb = postRepository
                .findById(id)
                .orElseThrow(() -> new DocumentNotFound(String.format(ExceptionMessage.NOT_FOUND, "Post", id)));

        Author author_posted = postInDb.getAuthor();
        Author loggedIn_author = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!author_posted.getId().equals(loggedIn_author.getId())) {
            throw new DocumentForbidden(AuthMessage.NOT_AUTHORIZED);
        }

        postInDb.setTitle(request.getTitle());
        postInDb.setText(request.getText());
        postInDb.setCategory(request.getCategory());
        postInDb.setTags(request.getTags());
        postInDb.setLatestChangeAt(LocalDateTime.now());

        Post savedPost = postRepository.save(postInDb);

        String senderMessage = "Dear " + loggedIn_author.getUsername() + ", you successfully updated post " + postInDb.getTitle() + ".";
        kafkaTemplate.send(NotificationMessage.POST_NOTIFICATION, senderMessage);


        PostDto postDto = postDtoConverter.convert(savedPost);
        List<Author> followers = loggedIn_author.getFollowers();

        for (Author follower: followers) {
            String receiverMessage = "Dear " + follower.getUsername() + ", author " + loggedIn_author.getUsername() + " updated the article " + postInDb.getTitle() + ".";
            kafkaTemplate.send(NotificationMessage.POST_NOTIFICATION, receiverMessage);
        }

        return Response.builder()
                .success(true)
                .message(AuthMessage.SUCCESS)
                .data(postDto)
                .build();
    }

    @Override
    public Response<Object> getPosts() {
        List<Post> posts = postRepository.findAll();

         return Response.builder()
                 .success(true)
                 .message(AuthMessage.SUCCESS)
                 .data(posts.stream()
                         .map(postDtoConverter::convert)
                         .collect(Collectors.toList()))
                 .build();
    }

    @Override
    public Response<Object> getPost(String id) {
        Post postInDb = postRepository
                .findById(id)
                .orElseThrow(() -> new DocumentNotFound(String.format(ExceptionMessage.NOT_FOUND, "Post", id)));

        return Response.builder()
                .success(true)
                .message(AuthMessage.SUCCESS)
                .data(postDtoConverter.convert(postInDb))
                .build();
    }

    @Override
    public Response<Object> deletePost(String id) {
        Post postInDb = postRepository
                .findById(id)
                .orElseThrow(() -> new DocumentNotFound(String.format(ExceptionMessage.NOT_FOUND, "Post", id)));

        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<Role> loggedInAuthorRoles = loggedInAuthor.getRoles();
        Set<RoleName> roles = loggedInAuthorRoles.stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        if (!roles.contains(RoleName.ADMIN) && postInDb.getAuthor().getId().equals(loggedInAuthor.getId())) {
            throw new DocumentForbidden(AuthMessage.NOT_AUTHORIZED);
        }
        postRepository.deleteById(id);
        String message = "Post " + id + " is successfully deleted";

        return Response.builder()
                .success(true)
                .message(AuthMessage.SUCCESS)
                .data(message)
                .build();
    }

    @Override
    public void addCommentToPost(String id, Comment comment) {
        Post postInDb = postRepository
                .findById(id)
                .orElseThrow(() -> new DocumentNotFound(String.format(ExceptionMessage.NOT_FOUND, "Post", id)));

        postInDb.getComments().add(comment);
        postRepository.save(postInDb);
    }

    @Override
    public void deleteCommentFromPost(String postId, String commentId) {
        Post postInDb = postRepository
                .findById(postId)
                .orElseThrow(() -> new DocumentNotFound(String.format(ExceptionMessage.NOT_FOUND, "Post", postId)));

        Comment commentInPost = postInDb
                .getComments()
                .stream()
                .filter(comment -> comment.getId().equals(commentId))
                .findAny()
                .orElseThrow(() -> new DocumentNotFound("Comment " + commentId + " is not found inside Post " + postId));

        postInDb.getComments().remove(commentInPost);
        postRepository.save(postInDb);
    }

    @Override
    public Response<Object> likePost(String id) {
        Post postInDb = postRepository
                .findById(id)
                .orElseThrow(() -> new DocumentNotFound(String.format(ExceptionMessage.NOT_FOUND, "Post", id)));

        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean isLiked = postInDb
                .getAuthorsLiked()
                .stream()
                .anyMatch(author -> author.getId().equals(loggedInAuthor.getId()));

        if (isLiked) {
            throw new DocumentConflict("You have already liked the post");
        }

        postInDb.getAuthorsDisliked().remove(loggedInAuthor);
        postInDb.getAuthorsLiked().add(loggedInAuthor);
        postRepository.save(postInDb);

        String message = "You liked post " + postInDb.getId();

        return Response.builder()
                .success(true)
                .message(AuthMessage.SUCCESS)
                .data(message)
                .build();
    }

    @Override
    public Response<Object> dislikePost(String id) {
        Post postInDb = postRepository
                .findById(id)
                .orElseThrow(() -> new DocumentNotFound(String.format(ExceptionMessage.NOT_FOUND, "Post", id)));

        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean isDisliked = postInDb
                .getAuthorsDisliked()
                .stream()
                .anyMatch(author -> author.getId().equals(loggedInAuthor.getId()));

        if (isDisliked) {
            throw new DocumentConflict("You have already disliked the post");
        }

        postInDb.getAuthorsLiked().remove(loggedInAuthor);
        postInDb.getAuthorsDisliked().add(loggedInAuthor);
        postRepository.save(postInDb);

        String message = "You disliked post " + postInDb.getId();

        return Response.builder()
                .success(true)
                .message(AuthMessage.SUCCESS)
                .data(message)
                .build();
    }

    @Override
    public Response<Object> removeStatus(String id) {
        Post postInDb = postRepository
                .findById(id)
                .orElseThrow(() -> new DocumentNotFound(String.format(ExceptionMessage.NOT_FOUND, "Post", id)));

        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean isLiked = postInDb
                .getAuthorsLiked()
                .stream()
                .anyMatch(author -> author.getId().equals(loggedInAuthor.getId()));

        boolean isDisliked = postInDb
                .getAuthorsDisliked()
                .stream()
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

        String message = "Your " + status + " for post " + postInDb.getId() + " is removed";

        return Response.builder()
                .success(true)
                .message(AuthMessage.SUCCESS)
                .data(message)
                .build();
    }

    @Override
    public Response<Object> getAuthorsLiked(String id) {
        Post postInDb = postRepository
                .findById(id)
                .orElseThrow(() -> new DocumentNotFound(String.format(ExceptionMessage.NOT_FOUND, "Post", id)));

        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!postInDb.getAuthor().getId().equals(loggedInAuthor.getId())) {
            throw new DocumentForbidden(AuthMessage.NOT_AUTHORIZED);
        }

        List<Author> authors = postInDb.getAuthorsLiked();

        return Response.builder()
                .success(true)
                .message(AuthMessage.SUCCESS)
                .data(authors.stream()
                        .map(authorDtoConverter::convert)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public Response<Object> getAuthorsDisliked(String id) {
        Post postInDb = postRepository
                .findById(id)
                .orElseThrow(() -> new DocumentNotFound(String.format(ExceptionMessage.NOT_FOUND, "Post", id)));

        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!postInDb.getAuthor().getId().equals(loggedInAuthor.getId())) {
            throw new DocumentForbidden(AuthMessage.NOT_AUTHORIZED);
        }

        List<Author> authors = postInDb.getAuthorsDisliked();

        return Response.builder()
                .success(true)
                .message(AuthMessage.SUCCESS)
                .data(authors.stream()
                        .map(authorDtoConverter::convert)
                        .collect(Collectors.toList()))
                .build();
    }

}
