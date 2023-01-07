package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.dto.CommentDto;
import com.ercanbeyen.bloggingplatform.dto.converter.CommentDtoConverter;
import com.ercanbeyen.bloggingplatform.entity.Comment;
import com.ercanbeyen.bloggingplatform.exception.DocumentNotFound;
import com.ercanbeyen.bloggingplatform.repository.CommentRepository;
import com.ercanbeyen.bloggingplatform.service.CommentService;
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

    @Override
    public CommentDto createComment(CommentDto commentDto) {
        Comment createdComment = Comment.builder()
                .author(commentDto.getAuthor())
                .text(commentDto.getText())
                .latestChangeAt(LocalDateTime.now())
                .build();

        return commentDtoConverter.convert(commentRepository.save(createdComment));
    }

    @Override
    public CommentDto updateComment(String id, CommentDto commentDto) {
        Comment commentInDb = commentRepository.findById(id)
                .orElseThrow(
                        () -> new DocumentNotFound("Comment " + id + " is not found")
                );

        commentInDb.setText(commentDto.getText());
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
    public String deleteComment(String id) {
        commentRepository.deleteById(id);
        return "Comment " + id + " is successfully deleted";
    }
}
