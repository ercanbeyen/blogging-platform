package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.entity.Ticket;

import java.util.List;

public interface TicketService {
    String createTicket(Ticket ticket);
    String updateTicket(Integer id, Ticket ticket);
    Ticket getTicket(Integer id);
    List<Ticket> getTickets();
    String deleteTicket(Integer id);
}
