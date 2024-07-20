package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.values.EntityName;
import com.ercanbeyen.bloggingplatform.constant.messages.NotificationMessage;
import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import com.ercanbeyen.bloggingplatform.entity.*;
import com.ercanbeyen.bloggingplatform.dto.CommentDto;
import com.ercanbeyen.bloggingplatform.dto.NotificationDto;
import com.ercanbeyen.bloggingplatform.dto.converter.CommentDtoConverter;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreateCommentRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateCommentRequest;
import com.ercanbeyen.bloggingplatform.exception.data.DataNotFound;
import com.ercanbeyen.bloggingplatform.repository.CommentRepository;
import com.ercanbeyen.bloggingplatform.service.CommentService;
import com.ercanbeyen.bloggingplatform.service.PostService;
import com.ercanbeyen.bloggingplatform.util.SecurityUtil;
import com.ercanbeyen.bloggingplatform.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        Author loggedInAuthor = SecurityUtil.getLoggedInAuthor();

        Comment newComment = Comment.builder()
                .author(loggedInAuthor)
                .text(request.getText())
                .latestChangeAt(TimeUtil.calculateNow())
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
        Comment commentInDb = findCommentById(id);
        Author authorCommented = commentInDb.getAuthor();
        SecurityUtil.checkAuthorAuthentication(authorCommented.getId());

        commentInDb.setText(request.getText());
        commentInDb.setLatestChangeAt(TimeUtil.calculateNow());

        return commentDtoConverter.convert(commentRepository.save(commentInDb));
    }

    @Override
    public List<CommentDto> getComments() {
        return commentRepository.findAll()
                .stream()
                .map(commentDtoConverter::convert)
                .toList();
    }

    @Override
    public CommentDto getComment(String id) {
        Comment commentInDb = findCommentById(id);
        return commentDtoConverter.convert(commentInDb);
    }

    @Transactional
    @Override
    public String deleteComment(String commentId, String postId) {
        Comment commentInDb = findCommentById(commentId);
        SecurityUtil.checkAuthorAuthentication(commentInDb.getAuthor().getId());

        postService.deleteCommentFromPost(postId, commentId);
        commentRepository.delete(commentInDb);

        return String.format(ResponseMessage.SUCCESS, EntityName.COMMENT, commentId, ResponseMessage.Operation.DELETED);
    }

    private Comment findCommentById(String id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, EntityName.COMMENT, id)));
    }
}
