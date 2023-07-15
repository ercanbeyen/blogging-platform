package com.ercanbeyen.bloggingplatform.dto.request.update;

import com.ercanbeyen.bloggingplatform.constant.enums.Gender;
import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import com.ercanbeyen.bloggingplatform.util.Location;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

@Getter
@Setter
@ToString
public class UpdateAuthorRequest {
    @NotBlank(message = "Username" + ResponseMessage.SHOULD_NOT_BLANK)
    private String username;
    @Email(message = ResponseMessage.INVALID_EMAIL_FORMAT)
    private String email;
    @NotBlank(message = "First name" + ResponseMessage.SHOULD_NOT_BLANK)
    private String firstName;
    @NotBlank(message = "Last name" + ResponseMessage.SHOULD_NOT_BLANK)
    private String lastName;
    private String about;
    private Gender gender;
    private Location location;
    private List<String> favoriteTopics;

}