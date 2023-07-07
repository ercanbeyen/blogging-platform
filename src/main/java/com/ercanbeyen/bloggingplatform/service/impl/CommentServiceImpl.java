package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.messages.NotificationMessage;
import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import com.ercanbeyen.bloggingplatform.constant.RoleName;
import com.ercanbeyen.bloggingplatform.document.*;
import com.ercanbeyen.bloggingplatform.dto.CommentDto;
import com.ercanbeyen.bloggingplatform.dto.NotificationDto;
import com.ercanbeyen.bloggingplatform.dto.converter.CommentDtoConverter;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreateCommentRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateCommentRequest;
import com.ercanbeyen.bloggingplatform.exception.DataForbidden;
import com.ercanbeyen.bloggingplatform.exception.DataNotFound;
import com.ercanbeyen.bloggingplatform.repository.CommentRepository;
import com.ercanbeyen.bloggingplatform.service.CommentService;
import com.ercanbeyen.bloggingplatform.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentDtoConverter commentDtoConverter;
    private final PostService postService;
    private final KafkaTemplate<String, NotificationDto> kafkaTemplate;

    @Transactional
    @Override
    public CommentDto createComment(CreateCommentRequest request) {
        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Comment newComment = Comment.builder()
                .author(loggedInAuthor)
                .text(request.getText())
                .latestChangeAt(LocalDateTime.now())
                .build();

        Comment commentInDb = commentRepository.save(newComment);
        postService.addCommentToPost(request.getPostId(), commentInDb);

        NotificationDto notificationDto = NotificationDto.builder()
                .fromAuthorId(loggedInAuthor.getId())
                .toAuthorId(postService.getPost(request.getPostId()).getAuthorId())
                .description(commentInDb.getText())
                .topic(NotificationMessage.COMMENT_NOTIFICATION)
                .build();

        kafkaTemplate.send(NotificationMessage.COMMENT_NOTIFICATION, notificationDto);

        return commentDtoConverter.convert(commentInDb);
    }

    @Transactional
    @Override
    public CommentDto updateComment(String id, UpdateCommentRequest request) {
        Comment commentInDb = commentRepository.findById(id)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, "Comment", id)));

        Author author_commented = commentInDb.getAuthor();
        Author loggedIn_author = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!author_commented.getId().equals(loggedIn_author.getId())) {
            throw new DataForbidden(ResponseMessage.NOT_AUTHORIZED);
        }

        commentInDb.setText(request.getText());
        commentInDb.setLatestChangeAt(LocalDateTime.now());

        return commentDtoConverter.convert(commentRepository.save(commentInDb));
    }

    @Override
    public List<CommentDto> getComments() {
        return commentRepository.findAll()
                .stream()
                .map(commentDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getComment(String id) {
        Comment commentInDb = commentRepository.findById(id)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, "Comment", id)));

        return commentDtoConverter.convert(commentInDb);
    }

    @Transactional
    @Override
    public String deleteComment(String commentId, String postId) {
        Comment commentInDb = commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, "Comment", commentId)));

        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<RoleName> roles = loggedInAuthor.getRoles()
                .stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        if (!roles.contains(RoleName.ADMIN) && !commentInDb.getAuthor().getId().equals(loggedInAuthor.getId())) {
            throw new DataForbidden(ResponseMessage.NOT_AUTHORIZED);
        }

        postService.deleteCommentFromPost(postId, commentId);
        commentRepository.deleteById(commentId);

        return "Comment " + commentId + " is successfully deleted";
    }

}
