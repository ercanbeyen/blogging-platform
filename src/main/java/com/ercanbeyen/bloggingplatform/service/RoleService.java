package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.constant.RoleName;
import com.ercanbeyen.bloggingplatform.document.Role;
import com.ercanbeyen.bloggingplatform.dto.RoleDto;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreateRoleRequest;

import java.util.List;

public interface RoleService {
    Role createRole(CreateRoleRequest request);
    List<RoleDto> getRoles();
    RoleDto getRole(String id);
    String deleteRole(String id);
    Role getRoleByRoleName(RoleName roleName);
}
