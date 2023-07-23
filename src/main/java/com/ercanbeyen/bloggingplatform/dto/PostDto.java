package com.ercanbeyen.bloggingplatform.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class PostDto extends RepresentationModel<EntityModel<PostDto>> {
    private String id;
    private String authorId;
    private String title;
    private String text;
    private String category;
    private List<String> tags;
    private LocalDateTime latestChangeAt;
}
