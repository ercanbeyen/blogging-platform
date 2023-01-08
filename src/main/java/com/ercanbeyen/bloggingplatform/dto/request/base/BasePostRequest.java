package com.ercanbeyen.bloggingplatform.dto.request.base;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class BasePostRequest {
    @NotBlank(message = "Title should not be blank")
    private String title;
    @NotBlank(message = "Text should not be blank")
    private String text;
    @NotBlank(message = "Category should not be blank")
    private String category;
    private List<String> tags;
}
