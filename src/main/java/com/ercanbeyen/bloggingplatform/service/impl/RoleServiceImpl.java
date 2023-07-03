package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import com.ercanbeyen.bloggingplatform.constant.RoleName;
import com.ercanbeyen.bloggingplatform.document.Role;
import com.ercanbeyen.bloggingplatform.dto.RoleDto;
import com.ercanbeyen.bloggingplatform.dto.converter.RoleDtoConverter;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreateRoleRequest;
import com.ercanbeyen.bloggingplatform.exception.DocumentConflict;
import com.ercanbeyen.bloggingplatform.exception.DocumentNotFound;
import com.ercanbeyen.bloggingplatform.repository.RoleRepository;
import com.ercanbeyen.bloggingplatform.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleDtoConverter roleDtoConverter;

    @Transactional
    @Override
    public Role createRole(CreateRoleRequest request) {
        Optional<Role> role = roleRepository.findByRoleName(request.getRoleName());

        if (role.isPresent()) {
            throw new DocumentConflict("Role " + request.getRoleName().name() + " is present");
        }

        Role newRole = Role.builder()
                .roleName(request.getRoleName())
                .build();

        return roleRepository.save(newRole);
    }

    @Override
    public List<RoleDto> getRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public RoleDto getRole(String id) {
        Role roleInDb = roleRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFound(String.format(ResponseMessage.NOT_FOUND, "Role", id)));

        return roleDtoConverter.convert(roleInDb);
    }

    @Transactional
    @Override
    public String deleteRole(String id) {
        boolean isIdFound = roleRepository.findById(id)
                .stream()
                .anyMatch(role -> role.getId().equals(id));

        if (!isIdFound) {
            throw new DocumentNotFound(String.format(ResponseMessage.NOT_FOUND, "Role", id));
        }

        roleRepository.deleteById(id);

        return "Role " + id + " is successfully deleted";
    }

    @Override
    public Role getRoleByRoleName(RoleName roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new DocumentNotFound(String.format(ResponseMessage.NOT_FOUND, "Role", roleName)));
    }
}
