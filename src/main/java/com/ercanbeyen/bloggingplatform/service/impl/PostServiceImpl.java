package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.dto.PostDto;
import com.ercanbeyen.bloggingplatform.dto.converter.PostDtoConverter;
import com.ercanbeyen.bloggingplatform.entity.Post;
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
    public PostDto createPost(PostDto postDto) {
        Post createdPost = Post.builder()
                .author(postDto.getAuthor())
                .title(postDto.getTitle())
                .text(postDto.getText())
                .category(postDto.getCategory())
                .numberOfLikes(postDto.getNumberOfLikes())
                .tags(postDto.getTags())
                .comments(postDto.getComments())
                .latestChangeAt(LocalDateTime.now())
                .build();
        return postDtoConverter.convert(postRepository.save(createdPost));
    }

    @Override
    public PostDto updatePost(String id, PostDto postDto) {
        Post postInDb = postRepository.findById(id)
                .orElseThrow(
                        () -> new DocumentNotFound("Post " + id + " is not found")
                );

        postInDb.setAuthor(postDto.getAuthor());
        postInDb.setTitle(postDto.getTitle());
        postInDb.setText(postDto.getText());
        postInDb.setCategory(postDto.getCategory());
        postInDb.setTags(postDto.getTags());

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
