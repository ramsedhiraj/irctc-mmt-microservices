package com.example.demo.mapper;

import org.springframework.stereotype.Component;
import com.example.demo.dto.response.BookingResponse;
import com.example.demo.entity.Ticket;

@Component
public class TicketMapper {

    public BookingResponse toResponse(Ticket ticket) {
        if (ticket == null) {
            return null;
        }
        BookingResponse response = new BookingResponse();
        response.setTicketId(ticket.getTicketId());
        response.setTrainNumber(ticket.getTrainNumber());
        response.setSource(ticket.getSource());
        response.setDestination(ticket.getDestination());
        response.setSeatNumber(ticket.getSeatNumber());
        response.setStatus(ticket.getStatus());
        response.setBookingDate(ticket.getBookingDate());
        if (ticket.getUser() != null) {
            response.setUserId(ticket.getUser().getId());
            response.setUserName(ticket.getUser().getName());
        }
        return response;
    }
}
