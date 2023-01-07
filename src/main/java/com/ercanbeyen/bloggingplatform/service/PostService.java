package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.dto.PostDto;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);
    PostDto updatePost(String id, PostDto postDto);
    List<PostDto> getPosts();
    PostDto getPost(String id);
    String deletePost(String id);
}
