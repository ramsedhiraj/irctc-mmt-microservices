package com.example.demo.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.BookingRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.BookingResponse;
import com.example.demo.service.IRCTCService;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final IRCTCService irctcService;

    public TicketController(IRCTCService irctcService) {
        this.irctcService = irctcService;
    }

    @PostMapping("/book")
    public ResponseEntity<ApiResponse<BookingResponse>> bookTicket(@Valid @RequestBody BookingRequest bookingRequest) {
        BookingResponse response = irctcService.bookTicket(bookingRequest);
        return new ResponseEntity<>(
            new ApiResponse<>(true, "Ticket booked successfully", response),
            HttpStatus.CREATED
        );
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<ApiResponse<BookingResponse>> getTicket(@PathVariable int ticketId) {
        BookingResponse response = irctcService.getTicket(ticketId);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Ticket retrieved successfully", response)
        );
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> cancelTicket(@PathVariable int ticketId) {
        irctcService.cancelTicket(ticketId);
        return ResponseEntity.noContent().build();
    }
}
