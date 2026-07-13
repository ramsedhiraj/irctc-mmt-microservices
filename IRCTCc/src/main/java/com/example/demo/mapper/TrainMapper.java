package com.example.demo.mapper;

import org.springframework.stereotype.Component;
import com.example.demo.dto.request.TrainRequest;
import com.example.demo.dto.response.TrainResponse;
import com.example.demo.entity.Train;

@Component
public class TrainMapper {

    public TrainResponse toResponse(Train train) {
        if (train == null) {
            return null;
        }
        TrainResponse response = new TrainResponse();
        response.setTrainNumber(train.getTrainNumber());
        response.setTrainName(train.getTrainName());
        response.setSource(train.getSource());
        response.setDestination(train.getDestination());
        response.setDepartureTime(train.getDepartureTime());
        response.setArrivalTime(train.getArrivalTime());
        response.setFare(train.getFare());
        response.setAvailableSeats(train.getAvailableSeats());
        return response;
    }

    public Train toEntity(TrainRequest request) {
        if (request == null) {
            return null;
        }
        Train train = new Train();
        train.setTrainNumber(request.getTrainNumber());
        train.setTrainName(request.getTrainName());
        train.setSource(request.getSource());
        train.setDestination(request.getDestination());
        train.setDepartureTime(request.getDepartureTime());
        train.setArrivalTime(request.getArrivalTime());
        train.setFare(request.getFare());
        train.setAvailableSeats(request.getAvailableSeats());
        return train;
    }

    public void updateEntity(TrainRequest request, Train train) {
        if (request == null || train == null) {
            return;
        }
        train.setTrainName(request.getTrainName());
        train.setSource(request.getSource());
        train.setDestination(request.getDestination());
        train.setDepartureTime(request.getDepartureTime());
        train.setArrivalTime(request.getArrivalTime());
        train.setFare(request.getFare());
        train.setAvailableSeats(request.getAvailableSeats());
    }
}
