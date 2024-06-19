package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.dto.ApprovalDto;
import com.ercanbeyen.bloggingplatform.dto.converter.ApprovalDtoConverter;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreateApprovalRequest;
import com.ercanbeyen.bloggingplatform.entity.Approval;
import com.ercanbeyen.bloggingplatform.entity.Ticket;
import com.ercanbeyen.bloggingplatform.mapper.ApprovalMapper;
import com.ercanbeyen.bloggingplatform.service.ApprovalService;
import com.ercanbeyen.bloggingplatform.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {
    private final ApprovalMapper approvalMapper;
    private final ApprovalDtoConverter converter;
    private final TicketService ticketService;

    @Override
    public String createApproval(CreateApprovalRequest request) {
        Ticket ticket = ticketService.getTicketById(request.ticketId());
        Approval approval = new Approval(request.authorId(), ticket);
        approvalMapper.insertApproval(approval);
        return "Approval is successfully created";
    }

    @Override
    public ApprovalDto getApproval(Integer id) {
        Approval approval = approvalMapper.findApprovalById(id);
        return converter.convert(approval);
    }

    @Override
    public List<ApprovalDto> getApprovals() {
        List<Approval> approvals = approvalMapper.findAllApprovals();
        return approvals
                .stream()
                .map(converter::convert)
                .toList();
    }

    @Override
    public String deleteApproval(Integer id) {
        approvalMapper.deleteApprovalById(id);
        return "Approval " + id + " is successfully deleted";
    }
}
