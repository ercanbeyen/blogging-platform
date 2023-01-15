package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.document.Comment;
import com.ercanbeyen.bloggingplatform.document.Post;
import com.ercanbeyen.bloggingplatform.dto.PostDto;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreatePostRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdatePostRequest;
import org.bson.types.ObjectId;

import java.util.List;

public interface PostService {
    PostDto createPost(CreatePostRequest request);
    PostDto updatePost(String id, UpdatePostRequest request);
    List<PostDto> getPosts();
    PostDto getPost(String id);
    String deletePost(String id);
    void addCommentToPost(String id, Comment comment);
    void deleteCommentFromPost(String postId, String commentId);
    String likePost(String id);
    String dislikePost(String id);
    String removeStatus(String id);
}
