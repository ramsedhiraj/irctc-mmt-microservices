package com.example.demo.dto.request;

import java.time.LocalTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class TrainRequest {

    @NotNull(message = "Train number must not be null")
    @Positive(message = "Train number must be positive")
    private Integer trainNumber;

    @NotBlank(message = "Train name must not be blank")
    private String trainName;

    @NotBlank(message = "Source must not be blank")
    private String source;

    @NotBlank(message = "Destination must not be blank")
    private String destination;

    @NotNull(message = "Departure time must not be null")
    private LocalTime departureTime;

    @NotNull(message = "Arrival time must not be null")
    private LocalTime arrivalTime;

    @NotNull(message = "Fare must not be null")
    @Positive(message = "Fare must be positive")
    private Integer fare;

    @NotNull(message = "Available seats must not be null")
    @PositiveOrZero(message = "Available seats must be zero or positive")
    private Integer availableSeats;

    
    public Integer getTrainNumber() {
        return trainNumber;
    }
    public void setTrainNumber(Integer trainNumber) {
        this.trainNumber = trainNumber;
    }
    public String getTrainName() {
        return trainName;
    }
    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public LocalTime getDepartureTime() {
        return departureTime;
    }
    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }
    public LocalTime getArrivalTime() {
        return arrivalTime;
    }
    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    public Integer getFare() {
        return fare;
    }
    public void setFare(Integer fare) {
        this.fare = fare;
    }
    public Integer getAvailableSeats() {
        return availableSeats;
    }
    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }
}
