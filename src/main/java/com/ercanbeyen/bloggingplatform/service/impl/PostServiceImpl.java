package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.dto.PostDto;
import com.ercanbeyen.bloggingplatform.dto.converter.PostDtoConverter;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreatePostRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdatePostRequest;
import com.ercanbeyen.bloggingplatform.document.Post;
import com.ercanbeyen.bloggingplatform.exception.DocumentNotFound;
import com.ercanbeyen.bloggingplatform.repository.PostRepository;
import com.ercanbeyen.bloggingplatform.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostDtoConverter postDtoConverter;

    @Override
    public PostDto createPost(CreatePostRequest request) {
        Post createdPost = Post.builder()
                .author("Trial")
                .title(request.getTitle())
                .text(request.getText())
                .category(request.getCategory())
                .tags(request.getTags())
                .latestChangeAt(LocalDateTime.now())
                .build();
        return postDtoConverter.convert(postRepository.save(createdPost));
    }

    @Override
    public PostDto updatePost(String id, UpdatePostRequest request) {
        Post postInDb = postRepository.findById(id)
                .orElseThrow(
                        () -> new DocumentNotFound("Post " + id + " is not found")
                );

        postInDb.setAuthor("Trial");
        postInDb.setTitle(request.getTitle());
        postInDb.setText(request.getText());
        postInDb.setCategory(request.getCategory());
        postInDb.setTags(request.getTags());
        postInDb.setLatestChangeAt(LocalDateTime.now());

        return postDtoConverter.convert(postRepository.save(postInDb));
    }

    @Override
    public List<PostDto> getPosts() {
        List<Post> posts = postRepository.findAll();

        return posts.stream()
                .map(postDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public PostDto getPost(String id) {
        Post postInDb = postRepository.findById(id)
                .orElseThrow(
                        () -> new DocumentNotFound("Post " + id + " is not found")
                );

        return postDtoConverter.convert(postInDb);
    }

    @Override
    public String deletePost(String id) {
        postRepository.deleteById(id);
        return "Post " + id + " is successfully deleted";
    }
}
