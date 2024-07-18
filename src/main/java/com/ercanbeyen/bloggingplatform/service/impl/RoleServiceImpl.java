package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.values.EntityName;
import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import com.ercanbeyen.bloggingplatform.constant.enums.RoleName;
import com.ercanbeyen.bloggingplatform.entity.Role;
import com.ercanbeyen.bloggingplatform.dto.RoleDto;
import com.ercanbeyen.bloggingplatform.dto.converter.RoleDtoConverter;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreateRoleRequest;
import com.ercanbeyen.bloggingplatform.exception.data.DataConflict;
import com.ercanbeyen.bloggingplatform.exception.data.DataNotFound;
import com.ercanbeyen.bloggingplatform.repository.RoleRepository;
import com.ercanbeyen.bloggingplatform.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
            throw new DataConflict(EntityName.ROLE + " " + request.getRoleName().name() + " is present");
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
                .toList();
    }

    @Override
    public RoleDto getRole(String id) {
        Role roleInDb = roleRepository.findById(id)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, EntityName.ROLE, id)));

        return roleDtoConverter.convert(roleInDb);
    }

    @Transactional
    @Override
    public String deleteRole(String id) {
        boolean isIdFound = roleRepository.findAll()
                .stream()
                .anyMatch(role -> role.getId().equals(id));

        if (!isIdFound) {
            throw new DataNotFound(String.format(ResponseMessage.NOT_FOUND, EntityName.ROLE, id));
        }

        roleRepository.deleteById(id);

        return String.format(ResponseMessage.SUCCESS, EntityName.ROLE, id, ResponseMessage.Operation.DELETED);
    }

    @Override
    public Role getRoleByRoleName(RoleName roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, EntityName.ROLE, roleName)));
    }
}
