package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.constant.enums.TicketStatus;
import com.ercanbeyen.bloggingplatform.dto.TicketDto;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreateTicketRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateTicketRequest;
import com.ercanbeyen.bloggingplatform.entity.Ticket;

import java.util.List;

public interface TicketService {
    String createTicket(CreateTicketRequest request);
    String updateTicket(Integer id, UpdateTicketRequest request);
    TicketDto getTicket(Integer id);
    List<TicketDto> getTickets(TicketStatus status, Integer createdYear, Integer updatedYear, Integer minimumNumberOfApprovals, String sortedField, String order, Integer numberOfTopApprovedTickets);
    String deleteTicket(Integer id);
    Ticket getTicketById(Integer id);
}
