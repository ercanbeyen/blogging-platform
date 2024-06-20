package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.dto.ApprovalDto;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreateApprovalRequest;

import java.util.List;

public interface ApprovalService {
    String createApproval(CreateApprovalRequest request);
    ApprovalDto getApproval(String id);
    List<ApprovalDto> getApprovals();
    String deleteApproval(String id);
}
