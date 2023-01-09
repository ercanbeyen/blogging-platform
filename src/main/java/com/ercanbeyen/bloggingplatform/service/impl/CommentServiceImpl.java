package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.document.Post;
import com.ercanbeyen.bloggingplatform.dto.CommentDto;
import com.ercanbeyen.bloggingplatform.dto.PostDto;
import com.ercanbeyen.bloggingplatform.dto.converter.CommentDtoConverter;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreateCommentRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateCommentRequest;
import com.ercanbeyen.bloggingplatform.document.Comment;
import com.ercanbeyen.bloggingplatform.exception.DocumentNotFound;
import com.ercanbeyen.bloggingplatform.repository.CommentRepository;
import com.ercanbeyen.bloggingplatform.service.CommentService;
import com.ercanbeyen.bloggingplatform.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentDtoConverter commentDtoConverter;
    private final PostService postService;

    @Override
    public CommentDto createComment(CreateCommentRequest request) {
        Comment createdComment = Comment.builder()
                .authorId("Trial")
                .text(request.getText())
                .latestChangeAt(LocalDateTime.now())
                .build();

        Comment commentInDb = commentRepository.save(createdComment);
        postService.addCommentToPost(request.getPostId(), commentInDb);

        return commentDtoConverter.convert(commentInDb);
    }

    @Override
    public CommentDto updateComment(String id, UpdateCommentRequest request) {
        Comment commentInDb = commentRepository.findById(id)
                .orElseThrow(
                        () -> new DocumentNotFound("Comment " + id + " is not found")
                );

        commentInDb.setText(request.getText());
        commentInDb.setLatestChangeAt(LocalDateTime.now());

        return commentDtoConverter.convert(commentRepository.save(commentInDb));
    }

    @Override
    public List<CommentDto> getComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .map(commentDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getComment(String id) {
        Comment commentInDb = commentRepository.findById(id)
                .orElseThrow(
                        () -> new DocumentNotFound("Comment " + id + " is not found")
                );

        return commentDtoConverter.convert(commentInDb);
    }

    @Override
    public String deleteComment(String commentId, String postId) {
        Comment commentInDb = commentRepository.findById(commentId)
                        .orElseThrow(
                                () -> new DocumentNotFound("Comment " + commentId + " is not found")
                        );

        postService.deleteCommentFromPost(postId, commentId);

        commentRepository.deleteById(commentId);
        return "Comment " + commentId + " is successfully deleted";
    }

}
