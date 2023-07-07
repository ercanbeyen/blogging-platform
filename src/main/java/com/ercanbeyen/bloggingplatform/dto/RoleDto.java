package com.ercanbeyen.bloggingplatform.dto;

import com.ercanbeyen.bloggingplatform.constant.enums.RoleName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDto {
    private RoleName roleName;
}
