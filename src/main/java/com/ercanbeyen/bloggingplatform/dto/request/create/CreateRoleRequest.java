package com.ercanbeyen.bloggingplatform.dto.request.create;

import com.ercanbeyen.bloggingplatform.constant.enums.RoleName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateRoleRequest {
    private RoleName roleName;
}
