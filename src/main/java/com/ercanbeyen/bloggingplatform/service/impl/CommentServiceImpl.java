package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.RoleName;
import com.ercanbeyen.bloggingplatform.document.Author;
import com.ercanbeyen.bloggingplatform.document.Post;
import com.ercanbeyen.bloggingplatform.document.Role;
import com.ercanbeyen.bloggingplatform.dto.CommentDto;
import com.ercanbeyen.bloggingplatform.dto.PostDto;
import com.ercanbeyen.bloggingplatform.dto.converter.CommentDtoConverter;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreateCommentRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateCommentRequest;
import com.ercanbeyen.bloggingplatform.document.Comment;
import com.ercanbeyen.bloggingplatform.exception.DocumentForbidden;
import com.ercanbeyen.bloggingplatform.exception.DocumentNotFound;
import com.ercanbeyen.bloggingplatform.repository.AuthorRepository;
import com.ercanbeyen.bloggingplatform.repository.CommentRepository;
import com.ercanbeyen.bloggingplatform.service.AuthorService;
import com.ercanbeyen.bloggingplatform.service.CommentService;
import com.ercanbeyen.bloggingplatform.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentDtoConverter commentDtoConverter;
    private final PostService postService;
    private final AuthorService authorService;

    @Override
    public CommentDto createComment(CreateCommentRequest request) {
        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Comment newComment = Comment.builder()
                .author(loggedInAuthor)
                .text(request.getText())
                .latestChangeAt(LocalDateTime.now())
                .build();

        Comment commentInDb = commentRepository.save(newComment);
        postService.addCommentToPost(request.getPostId(), commentInDb);

        return commentDtoConverter.convert(commentInDb);
    }

    @Override
    public CommentDto updateComment(String id, UpdateCommentRequest request) {
        Comment commentInDb = commentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFound("Comment " + id + " is not found"));

        Author author_commented = commentInDb.getAuthor();
        Author loggedIn_author = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!author_commented.getId().equals(loggedIn_author.getId())) {
            throw new DocumentForbidden("You are not authorized");
        }

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
                .orElseThrow(() -> new DocumentNotFound("Comment " + id + " is not found"));

        return commentDtoConverter.convert(commentInDb);
    }

    @Override
    public String deleteComment(String commentId, String postId) {
        Comment commentInDb = commentRepository.findById(commentId)
                .orElseThrow(() -> new DocumentNotFound("Comment " + commentId + " is not found"));

        Author loggedInAuthor = (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<Role> loggedInAuthorRoles = loggedInAuthor.getRoles();
        Set<RoleName> roles = loggedInAuthorRoles.stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        if (!roles.contains(RoleName.ADMIN) && commentInDb.getAuthor().getId().equals(loggedInAuthor.getId())) {
            throw new DocumentForbidden("You are not authorized");
        }

        postService.deleteCommentFromPost(postId, commentId);

        commentRepository.deleteById(commentId);
        return "Comment " + commentId + " is successfully deleted";
    }

}
