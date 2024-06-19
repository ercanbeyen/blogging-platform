package com.ercanbeyen.bloggingplatform.controller;

import com.ercanbeyen.bloggingplatform.dto.ApprovalDto;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreateApprovalRequest;
import com.ercanbeyen.bloggingplatform.service.ApprovalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/approvals")
@RequiredArgsConstructor
public class ApprovalController {
    private final ApprovalService approvalService;

    @PostMapping
    public ResponseEntity<String> createApproval(@RequestBody @Valid CreateApprovalRequest request) {
        return ResponseEntity.ok(approvalService.createApproval(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApprovalDto> getApproval(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(approvalService.getApproval(id));
    }

    @GetMapping
    public ResponseEntity<List<ApprovalDto>> getApprovals() {
        return ResponseEntity.ok(approvalService.getApprovals());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteApproval(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(approvalService.deleteApproval(id));
    }
}
