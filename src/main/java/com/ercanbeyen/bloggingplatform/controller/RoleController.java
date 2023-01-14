package com.ercanbeyen.bloggingplatform.controller;

import com.ercanbeyen.bloggingplatform.dto.RoleDto;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreateRoleRequest;
import com.ercanbeyen.bloggingplatform.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<RoleDto> createRole(@RequestBody CreateRoleRequest request) {
        return ResponseEntity.ok(roleService.createRole(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRole(@PathVariable("id") String id) {
        return ResponseEntity.ok(roleService.getRole(id));
    }

    @GetMapping
    public ResponseEntity<List<RoleDto>> getRoles() {
        return ResponseEntity.ok(roleService.getRoles());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable("id") String id) {
        return ResponseEntity.ok(roleService.deleteRole(id));
    }
}
