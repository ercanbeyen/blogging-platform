package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.document.Comment;
import com.ercanbeyen.bloggingplatform.document.Response;
import com.ercanbeyen.bloggingplatform.dto.CommentDto;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreateCommentRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateCommentRequest;

import java.util.List;

public interface CommentService {
    Response<Object> createComment(CreateCommentRequest request);
    Response<Object> updateComment(String id, UpdateCommentRequest request);
    Response<Object> getComments();
    Response<Object> getComment(String id);
    Response<Object> deleteComment(String commentId, String postId);
}
