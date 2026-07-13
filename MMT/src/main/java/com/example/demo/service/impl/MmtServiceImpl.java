package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.request.BookingRequest;
import com.example.demo.dto.request.TrainRequest;
import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.BookingResponse;
import com.example.demo.dto.response.TrainResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.service.MmtService;

@Service
public class MmtServiceImpl implements MmtService {

    private final RestTemplate restTemplate;
    private final String irctcBaseUrl;

    public MmtServiceImpl(RestTemplate restTemplate, @Value("${irctc.base-url}") String irctcBaseUrl) {
        this.restTemplate = restTemplate;
        this.irctcBaseUrl = irctcBaseUrl;
    }

    
    @Override
    public ApiResponse<UserResponse> createUser(UserRequest userRequest) {
        String url = irctcBaseUrl + "/api/v1/users";
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest);
        ResponseEntity<ApiResponse<UserResponse>> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            request,
            new ParameterizedTypeReference<ApiResponse<UserResponse>>() {}
        );
        return response.getBody();
    }

    @Override
    public ApiResponse<UserResponse> getUserById(int id) {
        String url = irctcBaseUrl + "/api/v1/users/" + id;
        ResponseEntity<ApiResponse<UserResponse>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<ApiResponse<UserResponse>>() {}
        );
        return response.getBody();
    }

    @Override
    public ApiResponse<UserResponse> updateUser(int id, UserRequest userRequest) {
        String url = irctcBaseUrl + "/api/v1/users/" + id;
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest);
        ResponseEntity<ApiResponse<UserResponse>> response = restTemplate.exchange(
            url,
            HttpMethod.PUT,
            request,
            new ParameterizedTypeReference<ApiResponse<UserResponse>>() {}
        );
        return response.getBody();
    }

    @Override
    public void deleteUser(int id) {
        String url = irctcBaseUrl + "/api/v1/users/" + id;
        restTemplate.delete(url);
    }

    @Override
    public ApiResponse<List<BookingResponse>> getUserBookings(int userId) {
        String url = irctcBaseUrl + "/api/v1/users/" + userId + "/tickets";
        ResponseEntity<ApiResponse<List<BookingResponse>>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<ApiResponse<List<BookingResponse>>>() {}
        );
        return response.getBody();
    }

    
    @Override
    public ApiResponse<TrainResponse> createTrain(TrainRequest trainRequest) {
        String url = irctcBaseUrl + "/api/v1/trains";
        HttpEntity<TrainRequest> request = new HttpEntity<>(trainRequest);
        ResponseEntity<ApiResponse<TrainResponse>> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            request,
            new ParameterizedTypeReference<ApiResponse<TrainResponse>>() {}
        );
        return response.getBody();
    }

    @Override
    public ApiResponse<List<TrainResponse>> searchTrains(String source, String destination) {
        String url = irctcBaseUrl + "/api/v1/trains/search?source=" + source + "&destination=" + destination;

        ResponseEntity<ApiResponse<List<TrainResponse>>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<ApiResponse<List<TrainResponse>>>() {}
        );
        return response.getBody();
    }

    @Override
    public ApiResponse<TrainResponse> getTrainByNumber(int trainNumber) {
        String url = irctcBaseUrl + "/api/v1/trains/" + trainNumber;
        ResponseEntity<ApiResponse<TrainResponse>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<ApiResponse<TrainResponse>>() {}
        );
        return response.getBody();
    }

    @Override
    public ApiResponse<TrainResponse> updateTrain(int trainNumber, TrainRequest trainRequest) {
        String url = irctcBaseUrl + "/api/v1/trains/" + trainNumber;
        HttpEntity<TrainRequest> request = new HttpEntity<>(trainRequest);
        ResponseEntity<ApiResponse<TrainResponse>> response = restTemplate.exchange(
            url,
            HttpMethod.PUT,
            request,
            new ParameterizedTypeReference<ApiResponse<TrainResponse>>() {}
        );
        return response.getBody();
    }

    @Override
    public void deleteTrain(int trainNumber) {
        String url = irctcBaseUrl + "/api/v1/trains/" + trainNumber;
        restTemplate.delete(url);
    }

    
    @Override
    public ApiResponse<BookingResponse> bookTicket(BookingRequest bookingRequest) {
        String url = irctcBaseUrl + "/api/v1/tickets/book";
        HttpEntity<BookingRequest> request = new HttpEntity<>(bookingRequest);
        ResponseEntity<ApiResponse<BookingResponse>> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            request,
            new ParameterizedTypeReference<ApiResponse<BookingResponse>>() {}
        );
        return response.getBody();
    }

    @Override
    public ApiResponse<BookingResponse> getTicket(int ticketId) {
        String url = irctcBaseUrl + "/api/v1/tickets/" + ticketId;
        ResponseEntity<ApiResponse<BookingResponse>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<ApiResponse<BookingResponse>>() {}
        );
        return response.getBody();
    }

    @Override
    public void cancelTicket(int ticketId) {
        String url = irctcBaseUrl + "/api/v1/tickets/" + ticketId;
        restTemplate.delete(url);
    }
}
