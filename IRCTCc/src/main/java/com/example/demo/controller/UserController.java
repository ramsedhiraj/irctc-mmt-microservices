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
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.BookingResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.service.IRCTCService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final IRCTCService irctcService;

    public UserController(IRCTCService irctcService) {
        this.irctcService = irctcService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse response = irctcService.createUser(userRequest);
        return new ResponseEntity<>(
            new ApiResponse<>(true, "User created successfully", response),
            HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable int id) {
        UserResponse response = irctcService.getUserById(id);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "User retrieved successfully", response)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable int id, @Valid @RequestBody UserRequest userRequest) {
        UserResponse response = irctcService.updateUser(id, userRequest);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "User updated successfully", response)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        irctcService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/tickets")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getUserBookings(@PathVariable int userId) {
        List<BookingResponse> response = irctcService.getUserBookings(userId);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "User bookings retrieved successfully", response)
        );
    }
}
