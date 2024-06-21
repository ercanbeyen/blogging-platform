package com.ercanbeyen.bloggingplatform.dto.converter;

import com.ercanbeyen.bloggingplatform.entity.Role;
import com.ercanbeyen.bloggingplatform.dto.RoleDto;
import org.springframework.stereotype.Component;

@Component
public class RoleDtoConverter {
    public RoleDto convert(Role role) {
        return RoleDto.builder()
                .roleName(role.getRoleName())
                .build();
    }
}
