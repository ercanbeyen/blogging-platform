package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import com.ercanbeyen.bloggingplatform.constant.values.EntityName;
import com.ercanbeyen.bloggingplatform.dto.ApprovalDto;
import com.ercanbeyen.bloggingplatform.dto.converter.ApprovalDtoConverter;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreateApprovalRequest;
import com.ercanbeyen.bloggingplatform.entity.Approval;
import com.ercanbeyen.bloggingplatform.entity.Ticket;
import com.ercanbeyen.bloggingplatform.exception.data.DataConflict;
import com.ercanbeyen.bloggingplatform.exception.data.DataNotFound;
import com.ercanbeyen.bloggingplatform.mapper.ApprovalMapper;
import com.ercanbeyen.bloggingplatform.service.ApprovalService;
import com.ercanbeyen.bloggingplatform.service.AuthorService;
import com.ercanbeyen.bloggingplatform.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {
    private final ApprovalMapper approvalMapper;
    private final ApprovalDtoConverter converter;
    private final AuthorService authorService;
    private final TicketService ticketService;

    @Override
    public String createApproval(CreateApprovalRequest request) {
        Ticket ticket = ticketService.getTicketById(request.ticketId());
        checkTicketBeforeApprove(request, ticket);
        Approval approval = new Approval(UUID.randomUUID().toString(), request.authorId(), ticket);
        approvalMapper.insertApproval(approval);
        return String.format(ResponseMessage.SUCCESS, ResponseMessage.State.NEW, EntityName.APPROVAL, ResponseMessage.Operation.CREATED);
    }

    @Override
    public ApprovalDto getApproval(String id) {
        return converter.convert(approvalMapper.findApprovalById(id));
    }

    @Override
    public List<ApprovalDto> getApprovals() {
        return approvalMapper.findAllApprovals()
                .stream()
                .map(converter::convert)
                .toList();
    }

    @Override
    public String deleteApproval(String id) {
        approvalMapper.deleteApprovalById(id);
        return String.format(ResponseMessage.SUCCESS, EntityName.APPROVAL, id, ResponseMessage.Operation.DELETED);
    }

    private void checkTicketBeforeApprove(CreateApprovalRequest request, Ticket ticket) {
        if (!authorService.authorExistsById(request.authorId())) {
            throw new DataNotFound(String.format(ResponseMessage.NOT_FOUND, EntityName.AUTHOR, request.authorId()));
        }

        boolean doesAuthorApprovedTicket = ticket.getApprovals()
                .stream()
                .map(Approval::getAuthorId)
                .anyMatch(authorId -> authorId.equals(request.authorId()));

        if (doesAuthorApprovedTicket) {
            throw new DataConflict(String.format("Author %s already approved ticket %d", request.authorId(), request.ticketId()));
        }
    }
}
