package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.entity.Approval;

import java.util.List;

public interface ApprovalService {
    String createApproval(Approval approval);
    Approval getApproval(Integer id);
    List<Approval> getApprovals();
    String deleteApproval(Integer id);
}
