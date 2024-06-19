package com.ercanbeyen.bloggingplatform.controller;

import com.ercanbeyen.bloggingplatform.entity.Approval;
import com.ercanbeyen.bloggingplatform.service.ApprovalService;
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
    public ResponseEntity<String> createApproval(@RequestBody Approval approval) {
        return ResponseEntity.ok(approvalService.createApproval(approval));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Approval> getApproval(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(approvalService.getApproval(id));
    }

    @GetMapping
    public ResponseEntity<List<Approval>> getApprovals() {
        return ResponseEntity.ok(approvalService.getApprovals());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteApproval(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(approvalService.deleteApproval(id));
    }
}
