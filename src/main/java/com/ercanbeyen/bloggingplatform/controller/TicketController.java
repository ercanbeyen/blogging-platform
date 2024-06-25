package com.ercanbeyen.bloggingplatform.controller;

import com.ercanbeyen.bloggingplatform.dto.TicketDto;
import com.ercanbeyen.bloggingplatform.dto.request.create.CreateTicketRequest;
import com.ercanbeyen.bloggingplatform.dto.request.update.UpdateTicketRequest;
import com.ercanbeyen.bloggingplatform.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets")
public class TicketController {
    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<String> createTicket(@RequestBody @Valid CreateTicketRequest request) {
        return ResponseEntity.ok(ticketService.createTicket(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTicket(@PathVariable("id") Integer id, @RequestBody @Valid UpdateTicketRequest request) {
        return ResponseEntity.ok(ticketService.updateTicket(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getTicket(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(ticketService.getTicket(id));
    }

    @GetMapping
    public ResponseEntity<List<TicketDto>> getTickets(
            @RequestParam(name = "createdAt", required = false) Integer createdYear,
            @RequestParam(name = "updatedAt", required = false) Integer updatedYear,
            @RequestParam(name = "minimumApprovals", required = false) Integer minimumNumberOfApprovals,
            @RequestParam(name = "sort", required = false) String sortedField,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "top", required = false) Integer numberOfTopApprovedTickets) {
        return ResponseEntity.ok(
                ticketService.getTickets(
                        createdYear,
                        updatedYear,
                        minimumNumberOfApprovals,
                        sortedField,
                        order,
                        numberOfTopApprovedTickets
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTicket(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(ticketService.deleteTicket(id));
    }
}
