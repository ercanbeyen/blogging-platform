package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.RoleName;
import com.ercanbeyen.bloggingplatform.document.Role;
import com.ercanbeyen.bloggingplatform.dto.RoleDto;
import com.ercanbeyen.bloggingplatform.dto.converter.RoleDtoConverter;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreateRoleRequest;
import com.ercanbeyen.bloggingplatform.exception.DocumentNotFound;
import com.ercanbeyen.bloggingplatform.repository.RoleRepository;
import com.ercanbeyen.bloggingplatform.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public RoleDto createRole(CreateRoleRequest request) {
        Optional<Role> role = roleRepository.findByRoleName(request.getRoleName());

        if (role.isPresent()) {
            return roleDtoConverter.convert(role.get());
        }

        Role newRole = Role.builder()
                .roleName(request.getRoleName())
                .build();

        Role createdRole = roleRepository.save(newRole);
        return roleDtoConverter.convert(createdRole);
    }

    @Override
    public List<RoleDto> getRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(roleDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public RoleDto getRole(String id) {
        Role roleInDb = roleRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFound("Role " + id + " is not found"));

        return roleDtoConverter.convert(roleInDb);
    }

    @Override
    public String deleteRole(String id) {
        Role roleInDb = roleRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFound("Role " + id + " is not found"));

        String roleId = roleInDb.getId();

        roleRepository.deleteById(id);
        return "Role " + roleId + " is successfully deleted";

    }

    @Override
    public Role getRoleById(String id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFound("Role " + id + " is not found"));
    }

    @Override
    public Role getRoleByRoleName(RoleName roleName) {
        return  roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new DocumentNotFound("Role " + roleName + " is not found"));
    }
}
