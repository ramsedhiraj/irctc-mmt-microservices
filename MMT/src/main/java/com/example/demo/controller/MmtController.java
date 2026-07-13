package com.example.demo.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.BookingRequest;
import com.example.demo.dto.request.TrainRequest;
import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.BookingResponse;
import com.example.demo.dto.response.TrainResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.service.MmtService;

@RestController
@RequestMapping("/api/v1/mmt")
public class MmtController {

    private final MmtService mmtService;

    public MmtController(MmtService mmtService) {
        this.mmtService = mmtService;
    }

    
    @PostMapping("/users")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserRequest userRequest) {
        ApiResponse<UserResponse> response = mmtService.createUser(userRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable int id) {
        ApiResponse<UserResponse> response = mmtService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable int id, @Valid @RequestBody UserRequest userRequest) {
        ApiResponse<UserResponse> response = mmtService.updateUser(id, userRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        mmtService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{userId}/tickets")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getUserBookings(@PathVariable int userId) {
        ApiResponse<List<BookingResponse>> response = mmtService.getUserBookings(userId);
        return ResponseEntity.ok(response);
    }

    
    @PostMapping("/trains")
    public ResponseEntity<ApiResponse<TrainResponse>> createTrain(@Valid @RequestBody TrainRequest trainRequest) {
        ApiResponse<TrainResponse> response = mmtService.createTrain(trainRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/trains/search")
    public ResponseEntity<ApiResponse<List<TrainResponse>>> searchTrains(@RequestParam String source, @RequestParam String destination) {
        ApiResponse<List<TrainResponse>> response = mmtService.searchTrains(source, destination);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/trains/{trainNumber}")
    public ResponseEntity<ApiResponse<TrainResponse>> getTrainByNumber(@PathVariable int trainNumber) {
        ApiResponse<TrainResponse> response = mmtService.getTrainByNumber(trainNumber);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/trains/{trainNumber}")
    public ResponseEntity<ApiResponse<TrainResponse>> updateTrain(@PathVariable int trainNumber, @Valid @RequestBody TrainRequest trainRequest) {
        ApiResponse<TrainResponse> response = mmtService.updateTrain(trainNumber, trainRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/trains/{trainNumber}")
    public ResponseEntity<Void> deleteTrain(@PathVariable int trainNumber) {
        mmtService.deleteTrain(trainNumber);
        return ResponseEntity.noContent().build();
    }

    
    @PostMapping("/tickets/book")
    public ResponseEntity<ApiResponse<BookingResponse>> bookTicket(@Valid @RequestBody BookingRequest bookingRequest) {
        ApiResponse<BookingResponse> response = mmtService.bookTicket(bookingRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/tickets/{ticketId}")
    public ResponseEntity<ApiResponse<BookingResponse>> getTicket(@PathVariable int ticketId) {
        ApiResponse<BookingResponse> response = mmtService.getTicket(ticketId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/tickets/{ticketId}")
    public ResponseEntity<Void> cancelTicket(@PathVariable int ticketId) {
        mmtService.cancelTicket(ticketId);
        return ResponseEntity.noContent().build();
    }
}
