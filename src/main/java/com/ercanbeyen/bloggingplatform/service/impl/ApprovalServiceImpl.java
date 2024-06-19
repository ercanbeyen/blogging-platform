package com.ercanbeyen.bloggingplatform.service.impl;

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
    private final TicketService ticketService;

    @Override
    public String createApproval(Approval approval) {
        Ticket ticket = ticketService.getTicket(approval.getTicketId());
        approval.setTicket(ticket);
        approvalMapper.insertApproval(approval);
        return "Approval is successfully created";
    }

    @Override
    public Approval getApproval(Integer id) {
        return approvalMapper.findApprovalById(id);
    }

    @Override
    public List<Approval> getApprovals() {
        return approvalMapper.findAllApprovals();
    }

    @Override
    public String deleteApproval(Integer id) {
        approvalMapper.deleteApprovalById(id);
        return "Approval " + id + " is successfully deleted";
    }
}
