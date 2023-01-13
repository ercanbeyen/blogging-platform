package com.ercanbeyen.bloggingplatform.dto.converter;

import com.ercanbeyen.bloggingplatform.document.Role;
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
