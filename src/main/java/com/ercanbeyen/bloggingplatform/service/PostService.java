package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.document.Comment;
import com.ercanbeyen.bloggingplatform.document.Post;
import com.ercanbeyen.bloggingplatform.document.Response;
import com.ercanbeyen.bloggingplatform.dto.AuthorDto;
import com.ercanbeyen.bloggingplatform.dto.PostDto;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreatePostRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdatePostRequest;
import org.bson.types.ObjectId;

import java.util.List;

public interface PostService {
    Response<Object> createPost(CreatePostRequest request);
    Response<Object> updatePost(String id, UpdatePostRequest request);
    Response<Object> getPosts();
    Response<Object> getPost(String id);
    Response<Object> deletePost(String id);
    void addCommentToPost(String id, Comment comment);
    void deleteCommentFromPost(String postId, String commentId);
    Response<Object> likePost(String id);
    Response<Object> dislikePost(String id);
    Response<Object> removeStatus(String id);
    Response<Object> getAuthorsLiked(String id);
    Response<Object> getAuthorsDisliked(String id);
}
