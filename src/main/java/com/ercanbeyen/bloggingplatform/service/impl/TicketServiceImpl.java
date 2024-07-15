package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.enums.RoleName;
import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import com.ercanbeyen.bloggingplatform.constant.values.EntityName;
import com.ercanbeyen.bloggingplatform.dto.TicketDto;
import com.ercanbeyen.bloggingplatform.dto.converter.TicketDtoConverter;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreateTicketRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateTicketRequest;
import com.ercanbeyen.bloggingplatform.entity.Role;
import com.ercanbeyen.bloggingplatform.entity.Ticket;
import com.ercanbeyen.bloggingplatform.exception.data.DataForbidden;
import com.ercanbeyen.bloggingplatform.exception.data.DataNotFound;
import com.ercanbeyen.bloggingplatform.mapper.TicketMapper;
import com.ercanbeyen.bloggingplatform.service.TicketService;
import com.ercanbeyen.bloggingplatform.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketMapper ticketMapper;
    private final TicketDtoConverter converter;

    @Override
    public String createTicket(CreateTicketRequest request) {
        Ticket ticket = new Ticket(request.getDescription());
        ticketMapper.insertTicket(ticket);
        return String.format(ResponseMessage.SUCCESS, ResponseMessage.State.NEW, EntityName.TICKET, ResponseMessage.Operation.CREATED);
    }

    @Override
    public String updateTicket(Integer id, UpdateTicketRequest request) {
        Ticket ticket = findTicketById(id);

        if (request.getStatus() != null) {
            checkAdminRole();
            ticket.setStatus(request.getStatus().name());
        }

        ticket.setDescription(request.getDescription());
        ticketMapper.updateTicket(id, ticket);

        return String.format(ResponseMessage.SUCCESS, EntityName.TICKET, id, ResponseMessage.Operation.UPDATED);
    }

    @Override
    public TicketDto getTicket(Integer id) {
        Ticket ticket = ticketMapper.findTicketById(id);
        return converter.convert(ticket);
    }

    @Override
    public List<TicketDto> getTickets(Integer createdYear, Integer updatedYear, Integer minimumNumberOfApprovals, String sortedField, String order, Integer numberOfTopApprovedTickets) {
        List<Ticket> tickets = ticketMapper.findAllTickets(createdYear, updatedYear, minimumNumberOfApprovals, sortedField, order, numberOfTopApprovedTickets);
        return tickets
                .stream()
                .map(converter::convert)
                .toList();
    }

    @Override
    public String deleteTicket(Integer id) {
        ticketMapper.deleteTicketById(id);
        return String.format(ResponseMessage.SUCCESS, EntityName.TICKET, id, ResponseMessage.Operation.DELETED);
    }

    @Override
    public Ticket getTicketById(Integer id) {
        return findTicketById(id);
    }

    private Ticket findTicketById(Integer id) {
        return Optional.ofNullable(ticketMapper.findTicketById(id))
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, EntityName.TICKET, id)));
    }

    private static void checkAdminRole() {
        boolean isAdmin = SecurityUtil.getLoggedInAuthor()
                .getRoles()
                .stream()
                .map(Role::getRoleName)
                .anyMatch(roleName -> roleName == RoleName.ADMIN);

        if (!isAdmin) {
            throw new DataForbidden(ResponseMessage.NOT_AUTHORIZED);
        }
    }
}
