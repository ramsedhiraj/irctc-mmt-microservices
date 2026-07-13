package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.dto.request.BookingRequest;
import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.BookingResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.service.IRCTCService;

public class IRCTCControllerTest {

    private IRCTCService irctcService;
    private UserController userController;
    private TicketController ticketController;
    private Validator validator;

    @BeforeEach
    public void setup() {
        irctcService = mock(IRCTCService.class);
        userController = new UserController(irctcService);
        ticketController = new TicketController(irctcService);
        
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testGetUserById() {
        UserResponse mockResponse = new UserResponse();
        mockResponse.setId(1);
        mockResponse.setName("John Doe");

        when(irctcService.getUserById(1)).thenReturn(mockResponse);

        ResponseEntity<ApiResponse<UserResponse>> response = userController.getUserById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John Doe", response.getBody().getData().getName());
    }

    @Test
    public void testCreateUserValidationFailed() {
        UserRequest request = new UserRequest();
        request.setName(""); 
        request.setAddress("Pune");
        request.setAadhar(-10); 
        request.setEmail("notanemail"); 

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBookTicketSuccess() {
        BookingRequest request = new BookingRequest();
        request.setUserId(1);
        request.setTrainNumber(12124);

        BookingResponse mockResponse = new BookingResponse();
        mockResponse.setTicketId(101);
        mockResponse.setStatus("CONFIRMED");

        when(irctcService.bookTicket(request)).thenReturn(mockResponse);

        ResponseEntity<ApiResponse<BookingResponse>> response = ticketController.bookTicket(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(101, response.getBody().getData().getTicketId());
    }
}
