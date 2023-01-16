package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.constant.RoleName;
import com.ercanbeyen.bloggingplatform.document.Response;
import com.ercanbeyen.bloggingplatform.document.Role;
import com.ercanbeyen.bloggingplatform.dto.RoleDto;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreateRoleRequest;

import java.util.List;

public interface RoleService {
    Response<Object> createRole(CreateRoleRequest request);
    Response<Object> getRoles();
    Response<Object> getRole(String id);
    Response<Object> deleteRole(String id);
    Role getRoleByRoleName(RoleName roleName);
}
