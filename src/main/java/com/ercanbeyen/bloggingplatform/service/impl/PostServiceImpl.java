package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.values.EntityName;
import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import com.ercanbeyen.bloggingplatform.constant.messages.NotificationMessage;
import com.ercanbeyen.bloggingplatform.entity.*;
import com.ercanbeyen.bloggingplatform.dto.AuthorDto;
import com.ercanbeyen.bloggingplatform.dto.CommentDto;
import com.ercanbeyen.bloggingplatform.dto.NotificationDto;
import com.ercanbeyen.bloggingplatform.dto.PostDto;
import com.ercanbeyen.bloggingplatform.dto.converter.AuthorDtoConverter;
import com.ercanbeyen.bloggingplatform.dto.converter.CommentDtoConverter;
import com.ercanbeyen.bloggingplatform.dto.converter.PostDtoConverter;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreatePostRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdatePostRequest;
import com.ercanbeyen.bloggingplatform.exception.data.DataConflict;
import com.ercanbeyen.bloggingplatform.exception.data.DataNotFound;
import com.ercanbeyen.bloggingplatform.repository.PostRepository;
import com.ercanbeyen.bloggingplatform.service.PostService;
import com.ercanbeyen.bloggingplatform.util.SecurityUtil;
import com.ercanbeyen.bloggingplatform.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostDtoConverter postDtoConverter;
    private final AuthorDtoConverter authorDtoConverter;
    private final CommentDtoConverter commentDtoConverter;
    private final KafkaTemplate<String, NotificationDto> kafkaTemplate;

    @Transactional
    @Override
    public PostDto createPost(CreatePostRequest request) {
        Author loggedInAuthor = SecurityUtil.getLoggedInAuthor();

        Post createdPost = Post.builder()
                .author(loggedInAuthor)
                .title(request.getTitle())
                .text(request.getText())
                .category(request.getCategory())
                .tags(request.getTags())
                .comments(new ArrayList<>())
                .latestChangeAt(TimeUtil.calculateNow())
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
        Post postInDb = findPostById(id);
        Author authorPosted = postInDb.getAuthor();
        SecurityUtil.checkAuthorAuthentication(authorPosted.getId());

        postInDb.setTitle(request.getTitle());
        postInDb.setText(request.getText());
        postInDb.setCategory(request.getCategory());
        postInDb.setTags(request.getTags());
        postInDb.setLatestChangeAt(TimeUtil.calculateNow());

        Post savedPost = postRepository.save(postInDb);

        return postDtoConverter.convert(savedPost);
    }

    @Override
    public List<PostDto> getPosts() {
        List<Post> posts = postRepository.findAll();

         return posts.stream()
                 .map(postDtoConverter::convert)
                 .toList();
    }

    @Override
    public PostDto getPost(String id) {
        Post postInDb = findPostById(id);
        return postDtoConverter.convert(postInDb);
    }

    @Transactional
    @Override
    public String deletePost(String id) {
        Post postInDb = findPostById(id);
        SecurityUtil.checkAuthorAuthentication(postInDb.getAuthor().getId());

        postRepository.delete(postInDb);

        return String.format(ResponseMessage.SUCCESS, EntityName.POST, id, ResponseMessage.Operation.DELETED);
    }

    @Transactional
    @Override
    public void addCommentToPost(String id, Comment comment) {
        Post postInDb = findPostById(id);

        postInDb.getComments().add(comment);
        postRepository.save(postInDb);
    }

    @Transactional
    @Override
    public void deleteCommentFromPost(String postId, String commentId) {
        Post postInDb = findPostById(postId);

        Comment commentInPost = postInDb.getComments()
                .stream()
                .filter(comment -> comment.getId().equals(commentId))
                .findAny()
                .orElseThrow(() -> new DataNotFound( EntityName.COMMENT + " " + commentId + " is not found inside " + EntityName.POST + " " + postId));

        postInDb.getComments().remove(commentInPost);
        postRepository.save(postInDb);
    }

    @Transactional
    @Override
    public String likePost(String id) {
        Post postInDb = findPostById(id);
        Author loggedInAuthor = SecurityUtil.getLoggedInAuthor();

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

    @Transactional
    @Override
    public String dislikePost(String id) {
        Post postInDb = postRepository.findById(id)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, EntityName.POST, id)));

        Author loggedInAuthor = SecurityUtil.getLoggedInAuthor();

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

    @Transactional
    @Override
    public String removeStatus(String id) {
        Post postInDb = findPostById(id);
        Author loggedInAuthor = SecurityUtil.getLoggedInAuthor();

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
        Post postInDb = findPostById(id);
        SecurityUtil.checkAuthorAuthentication(postInDb.getAuthor().getId());

        return postInDb.getAuthorsLiked()
                .stream()
                .map(authorDtoConverter::convert)
                .toList();
    }

    @Override
    public List<AuthorDto> getAuthorsDisliked(String id) {
        Post postInDb = findPostById(id);
        SecurityUtil.checkAuthorAuthentication(postInDb.getAuthor().getId());

        return postInDb.getAuthorsDisliked()
                .stream()
                .map(authorDtoConverter::convert)
                .toList();
    }

    @Override
    public List<CommentDto> getComments(String id) {
        Post postInDb = findPostById(id);

        return postInDb.getComments()
                .stream()
                .map(commentDtoConverter::convert)
                .toList();
    }

    private Post findPostById(String id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, EntityName.POST, id)));
    }
}
