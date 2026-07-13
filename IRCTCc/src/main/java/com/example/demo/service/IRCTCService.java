package com.example.demo.service;

import java.util.List;
import com.example.demo.dto.request.BookingRequest;
import com.example.demo.dto.request.TrainRequest;
import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.BookingResponse;
import com.example.demo.dto.response.TrainResponse;
import com.example.demo.dto.response.UserResponse;

public interface IRCTCService {

    
    UserResponse createUser(UserRequest userRequest);
    UserResponse getUserById(int userId);
    UserResponse updateUser(int userId, UserRequest userRequest);
    void deleteUser(int userId);

    
    TrainResponse createTrain(TrainRequest trainRequest);
    List<TrainResponse> searchTrains(String source, String destination);
    TrainResponse getTrainByNumber(int trainNumber);
    TrainResponse updateTrain(int trainNumber, TrainRequest trainRequest);
    void deleteTrain(int trainNumber);

    
    BookingResponse bookTicket(BookingRequest bookingRequest);
    BookingResponse getTicket(int ticketId);
    List<BookingResponse> getUserBookings(int userId);
    void cancelTicket(int ticketId);
}
