package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.Message;
import com.ercanbeyen.bloggingplatform.constant.RoleName;
import com.ercanbeyen.bloggingplatform.document.Response;
import com.ercanbeyen.bloggingplatform.document.Role;
import com.ercanbeyen.bloggingplatform.dto.converter.RoleDtoConverter;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreateRoleRequest;
import com.ercanbeyen.bloggingplatform.exception.DocumentConflict;
import com.ercanbeyen.bloggingplatform.exception.DocumentNotFound;
import com.ercanbeyen.bloggingplatform.repository.RoleRepository;
import com.ercanbeyen.bloggingplatform.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleDtoConverter roleDtoConverter;

    @Override
    public Response<Object> createRole(CreateRoleRequest request) {
        Optional<Role> role = roleRepository.findByRoleName(request.getRoleName());

        if (role.isPresent()) {
            throw new DocumentConflict("Role " + request.getRoleName().name() + " is present");
        }

        Role newRole = Role.builder()
                .roleName(request.getRoleName())
                .build();

        Role createdRole = roleRepository.save(newRole);

        return Response.builder()
                .success(true)
                .message(Message.SUCCESS)
                .data(roleDtoConverter.convert(createdRole))
                .build();
    }

    @Override
    public Response<Object> getRoles() {
        List<Role> roles = roleRepository.findAll();
        return Response.builder()
                .success(true)
                .message(Message.SUCCESS)
                .data(roles.stream()
                        .map(roleDtoConverter::convert)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public Response<Object> getRole(String id) {
        Role roleInDb = roleRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFound("Role " + id + " is not found"));

        return Response.builder()
                .success(true)
                .message(Message.SUCCESS)
                .data(roleDtoConverter.convert(roleInDb))
                .build();
    }

    @Override
    public Response<Object> deleteRole(String id) {
        Role roleInDb = roleRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFound("Role " + id + " is not found"));

        String roleId = roleInDb.getId();

        roleRepository.deleteById(id);
        String message = "Role " + roleId + " is successfully deleted";

        return Response.builder()
                .success(true)
                .message(Message.SUCCESS)
                .data(message)
                .build();
    }

    @Override
    public Role getRoleByRoleName(RoleName roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new DocumentNotFound("Role " + roleName + " is not found"));
    }
}
