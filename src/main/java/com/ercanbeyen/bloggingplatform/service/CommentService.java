package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.document.Comment;
import com.ercanbeyen.bloggingplatform.dto.CommentDto;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreateCommentRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateCommentRequest;

import java.util.List;

public interface CommentService {
    CommentDto createComment(CreateCommentRequest request);
    CommentDto updateComment(String id, UpdateCommentRequest request);
    List<CommentDto> getComments();
    CommentDto getComment(String id);
    String deleteComment(String commentId, String postId);
}
