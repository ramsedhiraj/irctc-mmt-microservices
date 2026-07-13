package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.request.BookingRequest;
import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.BookingResponse;
import com.example.demo.dto.response.TrainResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.service.impl.MmtServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MmtServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    private MmtService mmtService;
    private final String irctcBaseUrl = "http://localhost:8081";

    @BeforeEach
    public void setup() {
        mmtService = new MmtServiceImpl(restTemplate, irctcBaseUrl);
    }

    @Test
    public void testCreateUser() {
        UserRequest request = new UserRequest();
        request.setName("John");
        request.setEmail("john@example.com");

        UserResponse userResponse = new UserResponse();
        userResponse.setId(1);
        userResponse.setName("John");
        userResponse.setEmail("john@example.com");

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>(true, "User created", userResponse);
        ResponseEntity<ApiResponse<UserResponse>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

        when(restTemplate.exchange(
            eq(irctcBaseUrl + "/api/v1/users"),
            eq(HttpMethod.POST),
            any(HttpEntity.class),
            any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        ApiResponse<UserResponse> result = mmtService.createUser(request);
        assertEquals(true, result.isSuccess());
        assertEquals("John", result.getData().getName());
    }

    @Test
    public void testSearchTrains() {
        TrainResponse trainResponse = new TrainResponse();
        trainResponse.setTrainNumber(12124);
        trainResponse.setTrainName("Deccan Queen");

        ApiResponse<List<TrainResponse>> apiResponse = new ApiResponse<>(true, "Trains found", Collections.singletonList(trainResponse));
        ResponseEntity<ApiResponse<List<TrainResponse>>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        when(restTemplate.exchange(
            eq(irctcBaseUrl + "/api/v1/trains/search?source=Pune&destination=Mumbai"),
            eq(HttpMethod.GET),
            eq(null),
            any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        ApiResponse<List<TrainResponse>> result = mmtService.searchTrains("Pune", "Mumbai");
        assertEquals(true, result.isSuccess());
        assertEquals(1, result.getData().size());
        assertEquals(12124, result.getData().get(0).getTrainNumber());
    }

    @Test
    public void testBookTicket() {
        BookingRequest request = new BookingRequest();
        request.setUserId(1);
        request.setTrainNumber(12124);

        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setTicketId(101);
        bookingResponse.setStatus("CONFIRMED");

        ApiResponse<BookingResponse> apiResponse = new ApiResponse<>(true, "Booked", bookingResponse);
        ResponseEntity<ApiResponse<BookingResponse>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

        when(restTemplate.exchange(
            eq(irctcBaseUrl + "/api/v1/tickets/book"),
            eq(HttpMethod.POST),
            any(HttpEntity.class),
            any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        ApiResponse<BookingResponse> result = mmtService.bookTicket(request);
        assertEquals(true, result.isSuccess());
        assertEquals(101, result.getData().getTicketId());
    }

    @Test
    public void testCancelTicket() {
        mmtService.cancelTicket(101);
        verify(restTemplate, times(1)).delete(irctcBaseUrl + "/api/v1/tickets/101");
    }
}
