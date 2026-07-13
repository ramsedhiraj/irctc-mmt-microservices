package com.example.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BookingRequest {

    @NotNull(message = "User ID must not be null")
    @Positive(message = "User ID must be positive")
    private Integer userId;

    @NotNull(message = "Train number must not be null")
    @Positive(message = "Train number must be positive")
    private Integer trainNumber;

    
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getTrainNumber() {
        return trainNumber;
    }
    public void setTrainNumber(Integer trainNumber) {
        this.trainNumber = trainNumber;
    }
}
