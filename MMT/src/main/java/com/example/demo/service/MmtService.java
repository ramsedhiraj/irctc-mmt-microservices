package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.request.BookingRequest;
import com.example.demo.dto.request.TrainRequest;
import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.BookingResponse;
import com.example.demo.dto.response.TrainResponse;
import com.example.demo.dto.response.UserResponse;

public interface MmtService {

    
    ApiResponse<UserResponse> createUser(UserRequest userRequest);
    ApiResponse<UserResponse> getUserById(int id);
    ApiResponse<UserResponse> updateUser(int id, UserRequest userRequest);
    void deleteUser(int id);
    ApiResponse<List<BookingResponse>> getUserBookings(int userId);

    
    ApiResponse<TrainResponse> createTrain(TrainRequest trainRequest);
    ApiResponse<List<TrainResponse>> searchTrains(String source, String destination);
    ApiResponse<TrainResponse> getTrainByNumber(int trainNumber);
    ApiResponse<TrainResponse> updateTrain(int trainNumber, TrainRequest trainRequest);
    void deleteTrain(int trainNumber);

    
    ApiResponse<BookingResponse> bookTicket(BookingRequest bookingRequest);
    ApiResponse<BookingResponse> getTicket(int ticketId);
    void cancelTicket(int ticketId);
}
