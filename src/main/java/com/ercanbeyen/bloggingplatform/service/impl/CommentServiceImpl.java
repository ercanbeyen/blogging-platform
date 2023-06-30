package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.messages.AuthMessage;
import com.ercanbeyen.bloggingplatform.constant.RoleName;
import com.ercanbeyen.bloggingplatform.constant.messages.ExceptionMessage;
import com.ercanbeyen.bloggingplatform.document.*;
import com.ercanbeyen.bloggingplatform.dto.converter.CommentDtoConverter;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreateCommentRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateCommentRequest;
import com.ercanbeyen.bloggingplatform.exception.DocumentForbidden;
import com.ercanbeyen.bloggingplatform.exception.DocumentNotFound;
import com.ercanbeyen.bloggingplatform.repository.CommentRepository;
import com.ercanbeyen.bloggingplatform.service.CommentService;
import com.ercanbeyen.bloggingplatform.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    @Override
    public Response<Object> createComment(CreateCommentRequest request) {
        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Comment newComment = Comment.builder()
                .author(loggedInAuthor)
                .text(request.getText())
                .latestChangeAt(LocalDateTime.now())
                .build();

        Comment commentInDb = commentRepository.save(newComment);
        postService.addCommentToPost(request.getPostId(), commentInDb);

        return Response.builder()
                .success(true)
                .message(AuthMessage.SUCCESS)
                .data(commentDtoConverter.convert(commentInDb))
                .build();
    }

    @Override
    public Response<Object> updateComment(String id, UpdateCommentRequest request) {
        Comment commentInDb = commentRepository
                .findById(id)
                .orElseThrow(() -> new DocumentNotFound(String.format(ExceptionMessage.NOT_FOUND, "Comment", id)));

        Author author_commented = commentInDb.getAuthor();
        Author loggedIn_author = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!author_commented.getId().equals(loggedIn_author.getId())) {
            throw new DocumentForbidden(AuthMessage.NOT_AUTHORIZED);
        }

        commentInDb.setText(request.getText());
        commentInDb.setLatestChangeAt(LocalDateTime.now());

        return Response.builder()
                .success(true)
                .message(AuthMessage.SUCCESS)
                .data(commentDtoConverter.convert(commentRepository.save(commentInDb)))
                .build();
    }

    @Override
    public Response<Object> getComments() {
        List<Comment> comments = commentRepository.findAll();
        return Response.builder()
                .success(true)
                .message(AuthMessage.SUCCESS)
                .data(comments.stream()
                        .map(commentDtoConverter::convert)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public Response<Object> getComment(String id) {
        Comment commentInDb = commentRepository
                .findById(id)
                .orElseThrow(() -> new DocumentNotFound(String.format(ExceptionMessage.NOT_FOUND, "Comment", id)));

        return Response.builder()
                .success(true)
                .message(AuthMessage.SUCCESS)
                .data(commentDtoConverter.convert(commentInDb))
                .build();
    }

    @Override
    public Response<Object> deleteComment(String commentId, String postId) {
        Comment commentInDb = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new DocumentNotFound(String.format(ExceptionMessage.NOT_FOUND, "Comment", commentId)));

        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<Role> loggedInAuthorRoles = loggedInAuthor.getRoles();
        Set<RoleName> roles = loggedInAuthorRoles.stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        if (!roles.contains(RoleName.ADMIN) && !commentInDb.getAuthor().getId().equals(loggedInAuthor.getId())) {
            throw new DocumentForbidden(AuthMessage.NOT_AUTHORIZED);
        }

        postService.deleteCommentFromPost(postId, commentId);

        commentRepository.deleteById(commentId);
        String message = "Comment " + commentId + " is successfully deleted";

        return Response.builder()
                .success(true)
                .message(AuthMessage.SUCCESS)
                .data(message)
                .build();
    }

}
