package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import com.ercanbeyen.bloggingplatform.constant.values.EntityName;
import com.ercanbeyen.bloggingplatform.dto.TicketDto;
import com.ercanbeyen.bloggingplatform.dto.converter.TicketDtoConverter;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreateTicketRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateTicketRequest;
import com.ercanbeyen.bloggingplatform.entity.Ticket;
import com.ercanbeyen.bloggingplatform.mapper.TicketMapper;
import com.ercanbeyen.bloggingplatform.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        Ticket ticket = new Ticket(request.getDescription());
        ticketMapper.updateTicket(id, ticket);
        return String.format(ResponseMessage.SUCCESS, EntityName.TICKET, id, ResponseMessage.Operation.UPDATED);
    }

    @Override
    public TicketDto getTicket(Integer id) {
        Ticket ticket = ticketMapper.findTicketById(id);
        return converter.convert(ticket);
    }

    @Override
    public List<TicketDto> getTickets(Integer createdYear, Integer updatedYear) {
        List<Ticket> tickets = ticketMapper.findAllTickets(createdYear, updatedYear);
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
        return ticketMapper.findTicketById(id);
    }
}
