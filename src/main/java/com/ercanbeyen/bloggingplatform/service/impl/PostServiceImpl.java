package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.DocumentName;
import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import com.ercanbeyen.bloggingplatform.constant.enums.RoleName;
import com.ercanbeyen.bloggingplatform.constant.messages.NotificationMessage;
import com.ercanbeyen.bloggingplatform.document.*;
import com.ercanbeyen.bloggingplatform.dto.AuthorDto;
import com.ercanbeyen.bloggingplatform.dto.NotificationDto;
import com.ercanbeyen.bloggingplatform.dto.PostDto;
import com.ercanbeyen.bloggingplatform.dto.converter.AuthorDtoConverter;
import com.ercanbeyen.bloggingplatform.dto.converter.PostDtoConverter;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreatePostRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdatePostRequest;
import com.ercanbeyen.bloggingplatform.exception.DataConflict;
import com.ercanbeyen.bloggingplatform.exception.DataForbidden;
import com.ercanbeyen.bloggingplatform.exception.DataNotFound;
import com.ercanbeyen.bloggingplatform.repository.PostRepository;
import com.ercanbeyen.bloggingplatform.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostDtoConverter postDtoConverter;
    private final AuthorDtoConverter authorDtoConverter;
    private final KafkaTemplate<String, NotificationDto> kafkaTemplate;

    @Transactional
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

        Post savedPost = postRepository.save(createdPost);
        List<Author> followers = loggedInAuthor.getFollowers();

        for (Author follower : followers) {
            String receiverMessage = "Dear " + follower.getUsername() + ", author " + loggedInAuthor.getUsername() + " posted a new article.";

            NotificationDto notificationDto = NotificationDto.builder()
                    .fromAuthorId(loggedInAuthor.getId())
                    .toAuthorId(follower.getId())
                    .description(receiverMessage)
                    .topic(NotificationMessage.POST_NOTIFICATION)
                    .build();

            kafkaTemplate.send(NotificationMessage.POST_NOTIFICATION, notificationDto);
        }

        return postDtoConverter.convert(savedPost);
    }

    @Transactional
    @Override
    public PostDto updatePost(String id, UpdatePostRequest request) {
        Post postInDb = postRepository.findById(id)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, DocumentName.POST, id)));

        Author author_posted = postInDb.getAuthor();
        Author loggedIn_author = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!author_posted.getId().equals(loggedIn_author.getId())) {
            throw new DataForbidden(ResponseMessage.NOT_AUTHORIZED);
        }

        postInDb.setTitle(request.getTitle());
        postInDb.setText(request.getText());
        postInDb.setCategory(request.getCategory());
        postInDb.setTags(request.getTags());
        postInDb.setLatestChangeAt(LocalDateTime.now());

        Post savedPost = postRepository.save(postInDb);
        List<Author> followers = loggedIn_author.getFollowers();

        for (Author follower: followers) {
            String receiverMessage = "Dear " + follower.getUsername() + ", author " + loggedIn_author.getUsername() + " updated the article " + postInDb.getTitle() + ".";
            NotificationDto notificationDto = NotificationDto.builder()
                    .fromAuthorId(loggedIn_author.getId())
                    .toAuthorId(follower.getId())
                    .description(receiverMessage)
                    .topic(NotificationMessage.POST_NOTIFICATION)
                    .build();

            kafkaTemplate.send(NotificationMessage.POST_NOTIFICATION, notificationDto);
        }

        return postDtoConverter.convert(savedPost);
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
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, DocumentName.POST, id)));

        return postDtoConverter.convert(postInDb);
    }

    @Transactional
    @Override
    public String deletePost(String id) {
        Post postInDb = postRepository.findById(id)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, DocumentName.POST, id)));

        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<RoleName> roles = loggedInAuthor.getRoles()
                .stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        if (!roles.contains(RoleName.ADMIN) && postInDb.getAuthor().getId().equals(loggedInAuthor.getId())) {
            throw new DataForbidden(ResponseMessage.NOT_AUTHORIZED);
        }

        postRepository.deleteById(id);

        return DocumentName.POST + " " + id + " is successfully deleted";
    }

    @Transactional
    @Override
    public void addCommentToPost(String id, Comment comment) {
        Post postInDb = postRepository.findById(id)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, DocumentName.POST, id)));

        postInDb.getComments().add(comment);
        postRepository.save(postInDb);
    }

    @Override
    public void deleteCommentFromPost(String postId, String commentId) {
        Post postInDb = postRepository.findById(postId)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, DocumentName.POST, postId)));

        Comment commentInPost = postInDb.getComments()
                .stream()
                .filter(comment -> comment.getId().equals(commentId))
                .findAny()
                .orElseThrow(() -> new DataNotFound( DocumentName.COMMENT + " " + commentId + " is not found inside " + DocumentName.POST + " " + postId));

        postInDb.getComments().remove(commentInPost);
        postRepository.save(postInDb);
    }

    @Override
    public String likePost(String id) {
        Post postInDb = postRepository.findById(id)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, DocumentName.POST, id)));

        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean isLiked = postInDb.getAuthorsLiked()
                .stream()
                .anyMatch(author -> author.getId().equals(loggedInAuthor.getId()));

        if (isLiked) {
            throw new DataConflict("You have already liked the post");
        }

        postInDb.getAuthorsDisliked().remove(loggedInAuthor);
        postInDb.getAuthorsLiked().add(loggedInAuthor);
        postRepository.save(postInDb);

        return "You liked post " + postInDb.getId();
    }

    @Override
    public String dislikePost(String id) {
        Post postInDb = postRepository.findById(id)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, DocumentName.POST, id)));

        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean isDisliked = postInDb.getAuthorsDisliked()
                .stream()
                .anyMatch(author -> author.getId().equals(loggedInAuthor.getId()));

        if (isDisliked) {
            throw new DataConflict("You have already disliked the post");
        }

        postInDb.getAuthorsLiked().remove(loggedInAuthor);
        postInDb.getAuthorsDisliked().add(loggedInAuthor);
        postRepository.save(postInDb);

        return "You disliked post " + postInDb.getId();
    }

    @Override
    public String removeStatus(String id) {
        Post postInDb = postRepository.findById(id)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, DocumentName.POST, id)));

        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean isLiked = postInDb.getAuthorsLiked()
                .stream()
                .anyMatch(author -> author.getId().equals(loggedInAuthor.getId()));

        boolean isDisliked = postInDb.getAuthorsDisliked()
                .stream()
                .anyMatch(author -> author.getId().equals(loggedInAuthor.getId()));

        if (!isLiked && !isDisliked) {
            throw new DataConflict("You have neither liked nor disliked");
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
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, DocumentName.POST, id)));

        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!postInDb.getAuthor().getId().equals(loggedInAuthor.getId())) {
            throw new DataForbidden(ResponseMessage.NOT_AUTHORIZED);
        }

        return postInDb.getAuthorsLiked()
                .stream()
                .map(authorDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuthorDto> getAuthorsDisliked(String id) {
        Post postInDb = postRepository.findById(id)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, DocumentName.POST, id)));

        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!postInDb.getAuthor().getId().equals(loggedInAuthor.getId())) {
            throw new DataForbidden(ResponseMessage.NOT_AUTHORIZED);
        }

        return postInDb.getAuthorsDisliked()
                .stream()
                .map(authorDtoConverter::convert)
                .collect(Collectors.toList());
    }

}
