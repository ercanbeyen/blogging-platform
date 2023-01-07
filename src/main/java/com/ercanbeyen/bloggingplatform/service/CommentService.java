package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(CommentDto commentDto);
    CommentDto updateComment(String id, CommentDto commentDto);
    List<CommentDto> getComments();
    CommentDto getComment(String id);
    String deleteComment(String id);
}
