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

import com.example.demo.dto.request.TrainRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.TrainResponse;
import com.example.demo.service.IRCTCService;

@RestController
@RequestMapping("/api/v1/trains")
public class TrainController {

    private final IRCTCService irctcService;

    public TrainController(IRCTCService irctcService) {
        this.irctcService = irctcService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TrainResponse>> createTrain(@Valid @RequestBody TrainRequest trainRequest) {
        TrainResponse response = irctcService.createTrain(trainRequest);
        return new ResponseEntity<>(
            new ApiResponse<>(true, "Train created successfully", response),
            HttpStatus.CREATED
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TrainResponse>>> searchTrains(@RequestParam String source, @RequestParam String destination) {
        List<TrainResponse> response = irctcService.searchTrains(source, destination);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Trains retrieved successfully", response)
        );
    }

    @GetMapping("/{trainNumber}")
    public ResponseEntity<ApiResponse<TrainResponse>> getTrainByNumber(@PathVariable int trainNumber) {
        TrainResponse response = irctcService.getTrainByNumber(trainNumber);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Train retrieved successfully", response)
        );
    }

    @PutMapping("/{trainNumber}")
    public ResponseEntity<ApiResponse<TrainResponse>> updateTrain(@PathVariable int trainNumber, @Valid @RequestBody TrainRequest trainRequest) {
        TrainResponse response = irctcService.updateTrain(trainNumber, trainRequest);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Train updated successfully", response)
        );
    }

    @DeleteMapping("/{trainNumber}")
    public ResponseEntity<Void> deleteTrain(@PathVariable int trainNumber) {
        irctcService.deleteTrain(trainNumber);
        return ResponseEntity.noContent().build();
    }
}
