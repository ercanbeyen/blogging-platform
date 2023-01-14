package com.ercanbeyen.bloggingplatform.dto.request.update;


import com.ercanbeyen.bloggingplatform.dto.request.base.BasePostRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class UpdatePostRequest extends BasePostRequest {
    @NotBlank(message = "Author id should not be blank")
    private String authorId;
}
