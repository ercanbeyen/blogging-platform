package com.ercanbeyen.bloggingplatform.service.impl;

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

    @Override
    public String createTicket(Ticket ticket) {
        ticketMapper.insertTicket(ticket);
        return "Ticket is added successfully";
    }

    @Override
    public String updateTicket(Integer id, Ticket ticket) {
        ticketMapper.updateTicket(id, ticket);
        return "Ticket " + id + " is updated successfully";
    }

    @Override
    public Ticket getTicket(Integer id) {
        return ticketMapper.findTicketById(id);
    }

    @Override
    public List<Ticket> getTickets(Integer createdYear, Integer updatedYear) {
        return ticketMapper.findAllTickets(createdYear, updatedYear);
    }

    @Override
    public String deleteTicket(Integer id) {
        ticketMapper.deleteTicketById(id);
        return "Ticket " + id + " is deleted successfully";
    }
}
