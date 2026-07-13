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
import com.example.demo.service.MmtService;

public class MmtControllerTest {

    private MmtService mmtService;
    private MmtController mmtController;
    private Validator validator;

    @BeforeEach
    public void setup() {
        mmtService = mock(MmtService.class);
        mmtController = new MmtController(mmtService);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testGetUserById() {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(1);
        userResponse.setName("Amit");

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>(true, "Retrieved", userResponse);

        when(mmtService.getUserById(1)).thenReturn(apiResponse);

        ResponseEntity<ApiResponse<UserResponse>> response = mmtController.getUserById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Amit", response.getBody().getData().getName());
    }

    @Test
    public void testCreateUserValidationFailed() {
        UserRequest request = new UserRequest();
        request.setName(""); 
        request.setAddress("Pune");
        request.setAadhar(-5); 
        request.setEmail("invalid"); 

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBookTicketSuccess() {
        BookingRequest request = new BookingRequest();
        request.setUserId(1);
        request.setTrainNumber(12124);

        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setTicketId(101);
        bookingResponse.setStatus("CONFIRMED");

        ApiResponse<BookingResponse> apiResponse = new ApiResponse<>(true, "Booked", bookingResponse);

        when(mmtService.bookTicket(request)).thenReturn(apiResponse);

        ResponseEntity<ApiResponse<BookingResponse>> response = mmtController.bookTicket(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(101, response.getBody().getData().getTicketId());
    }
}
