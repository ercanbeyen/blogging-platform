package com.ercanbeyen.bloggingplatform.dto.request.create;

import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import com.ercanbeyen.bloggingplatform.dto.request.base.BaseCommentRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateCommentRequest extends BaseCommentRequest {
    @NotBlank(message = "Post id" + ResponseMessage.SHOULD_NOT_BLANK)
    private String postId;
}
