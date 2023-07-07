package com.ercanbeyen.bloggingplatform.dto.request.update;

import com.ercanbeyen.bloggingplatform.constant.enums.RoleName;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class UpdateAuthorRolesRequest {
    @NotEmpty(message = "Author should have a role")
    Set<RoleName> roles;
}
