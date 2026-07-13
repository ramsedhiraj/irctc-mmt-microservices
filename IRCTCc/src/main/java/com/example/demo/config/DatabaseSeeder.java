package com.example.demo.config;

import java.time.LocalTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Train;
import com.example.demo.entity.User;
import com.example.demo.repository.IRCTCTrainRepository;
import com.example.demo.repository.IRCTCUserRepository;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final IRCTCUserRepository userRepository;
    private final IRCTCTrainRepository trainRepository;

    public DatabaseSeeder(IRCTCUserRepository userRepository, IRCTCTrainRepository trainRepository) {
        this.userRepository = userRepository;
        this.trainRepository = trainRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        
        if (userRepository.count() == 0) {
            seedUser("Amit Sharma", "Pune", 11223344, "amit@example.com");
            seedUser("Priya Patel", "Mumbai", 22334455, "priya@example.com");
            seedUser("Rahul Verma", "Delhi", 33445566, "rahul@example.com");
            seedUser("Siddharth Sen", "Bangalore", 44556677, "sid@example.com");
            seedUser("Karan Rao", "Hyderabad", 55667788, "karan@example.com");
        }

        
        if (trainRepository.count() == 0) {
            
            seedTrain(12124, "Deccan Queen", "Pune", "Mumbai", LocalTime.of(7, 15), LocalTime.of(10, 25), 450, 100);
            
            
            seedTrain(12951, "Mumbai Rajdhani", "Mumbai", "Delhi", LocalTime.of(17, 0), LocalTime.of(8, 30), 2200, 5);
            
            
            seedTrain(12002, "New Delhi Shatabdi", "Delhi", "Bhopal", LocalTime.of(6, 0), LocalTime.of(14, 40), 950, 12);
            
            
            seedTrain(12028, "Shatabdi Express", "Bangalore", "Chennai", LocalTime.of(6, 0), LocalTime.of(11, 0), 750, 50);
            
            
            seedTrain(12026, "Pune Shatabdi", "Hyderabad", "Pune", LocalTime.of(14, 45), LocalTime.of(23, 10), 1100, 8);
            
            
            seedTrain(12025, "Secunderabad Shatabdi", "Pune", "Hyderabad", LocalTime.of(6, 0), LocalTime.of(14, 20), 1100, 20);

            
            seedTrain(12127, "Mumbai Pune Intercity", "Mumbai", "Pune", LocalTime.of(6, 40), LocalTime.of(9, 57), 350, 0);

            
            seedTrain(12952, "August Kranti Rajdhani", "Delhi", "Mumbai", LocalTime.of(16, 50), LocalTime.of(9, 55), 2100, 45);
        }
    }

    private void seedUser(String name, String address, int aadhar, String email) {
        User user = new User();
        user.setName(name);
        user.setAddress(address);
        user.setAadhar(aadhar);
        user.setEmail(email);
        userRepository.save(user);
    }

    private void seedTrain(int trainNumber, String name, String source, String dest, LocalTime dep, LocalTime arr, int fare, int seats) {
        Train train = new Train();
        train.setTrainNumber(trainNumber);
        train.setTrainName(name);
        train.setSource(source);
        train.setDestination(dest);
        train.setDepartureTime(dep);
        train.setArrivalTime(arr);
        train.setFare(fare);
        train.setAvailableSeats(seats);
        trainRepository.save(train);
    }
}
