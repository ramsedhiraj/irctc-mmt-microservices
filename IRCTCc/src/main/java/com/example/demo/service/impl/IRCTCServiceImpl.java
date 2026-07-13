package com.example.demo.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.request.BookingRequest;
import com.example.demo.dto.request.TrainRequest;
import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.BookingResponse;
import com.example.demo.dto.response.TrainResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.Ticket;
import com.example.demo.entity.Train;
import com.example.demo.entity.User;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.SeatNotAvailableException;
import com.example.demo.exception.TicketNotFoundException;
import com.example.demo.exception.TrainNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.mapper.TicketMapper;
import com.example.demo.mapper.TrainMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.IRCTCTicketRepository;
import com.example.demo.repository.IRCTCTrainRepository;
import com.example.demo.repository.IRCTCUserRepository;
import com.example.demo.service.IRCTCService;

@Service
public class IRCTCServiceImpl implements IRCTCService {

    private static final Logger log = LoggerFactory.getLogger(IRCTCServiceImpl.class);

    private final IRCTCUserRepository userRepository;
    private final IRCTCTrainRepository trainRepository;
    private final IRCTCTicketRepository ticketRepository;
    private final UserMapper userMapper;
    private final TrainMapper trainMapper;
    private final TicketMapper ticketMapper;

    public IRCTCServiceImpl(IRCTCUserRepository userRepository,
                            IRCTCTrainRepository trainRepository,
                            IRCTCTicketRepository ticketRepository,
                            UserMapper userMapper,
                            TrainMapper trainMapper,
                            TicketMapper ticketMapper) {
        this.userRepository = userRepository;
        this.trainRepository = trainRepository;
        this.ticketRepository = ticketRepository;
        this.userMapper = userMapper;
        this.trainMapper = trainMapper;
        this.ticketMapper = ticketMapper;
    }

    
    @Override
    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new ConflictException("User with email " + userRequest.getEmail() + " already exists");
        }
        User user = userMapper.toEntity(userRequest);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse getUserById(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateUser(int userId, UserRequest userRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        
        
        if (!user.getEmail().equalsIgnoreCase(userRequest.getEmail()) && userRepository.existsByEmail(userRequest.getEmail())) {
            throw new ConflictException("User with email " + userRequest.getEmail() + " already exists");
        }

        userMapper.updateEntity(userRequest, user);
        User updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        if (ticketRepository.existsByUser_Id(userId)) {
            throw new ConflictException("User cannot be deleted because they have active bookings");
        }
        userRepository.deleteById(userId);
    }

    
    @Override
    @Transactional
    public TrainResponse createTrain(TrainRequest trainRequest) {
        if (trainRepository.existsByTrainNumber(trainRequest.getTrainNumber())) {
            throw new ConflictException("Train with number " + trainRequest.getTrainNumber() + " already exists");
        }
        Train train = trainMapper.toEntity(trainRequest);
        Train savedTrain = trainRepository.save(train);
        return trainMapper.toResponse(savedTrain);
    }

    @Override
    public List<TrainResponse> searchTrains(String source, String destination) {
        List<Train> trains = trainRepository.findBySourceAndDestination(source, destination);
        return trains.stream()
                .map(trainMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TrainResponse getTrainByNumber(int trainNumber) {
        Train train = trainRepository.findByTrainNumber(trainNumber)
                .orElseThrow(() -> new TrainNotFoundException("Train not found with number: " + trainNumber));
        return trainMapper.toResponse(train);
    }

    @Override
    @Transactional
    public TrainResponse updateTrain(int trainNumber, TrainRequest trainRequest) {
        Train train = trainRepository.findByTrainNumber(trainNumber)
                .orElseThrow(() -> new TrainNotFoundException("Train not found with number: " + trainNumber));

        trainMapper.updateEntity(trainRequest, train);
        Train updatedTrain = trainRepository.save(train);
        return trainMapper.toResponse(updatedTrain);
    }

    @Override
    @Transactional
    public void deleteTrain(int trainNumber) {
        Train train = trainRepository.findByTrainNumber(trainNumber)
                .orElseThrow(() -> new TrainNotFoundException("Train not found with number: " + trainNumber));

        if (ticketRepository.existsByTrain_TrainNumber(trainNumber)) {
            throw new ConflictException("Train cannot be deleted because it has active bookings");
        }
        trainRepository.delete(train);
    }

    
    @Override
    @Transactional
    public BookingResponse bookTicket(BookingRequest bookingRequest) {
        log.info("Booking started");
        try {
            User user = userRepository.findById(bookingRequest.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + bookingRequest.getUserId()));

            Train train = trainRepository.findByTrainNumber(bookingRequest.getTrainNumber())
                    .orElseThrow(() -> new TrainNotFoundException("Train not found with number: " + bookingRequest.getTrainNumber()));

            if (train.getAvailableSeats() <= 0) {
                throw new SeatNotAvailableException("No seats available in train " + train.getTrainNumber());
            }

            
            int allocatedSeat = train.getAvailableSeats();
            
            
            train.setAvailableSeats(train.getAvailableSeats() - 1);
            trainRepository.save(train);

            Ticket ticket = new Ticket();
            ticket.setTrainNumber(train.getTrainNumber());
            ticket.setSource(train.getSource());
            ticket.setDestination(train.getDestination());
            ticket.setSeatNumber(allocatedSeat);
            ticket.setStatus("CONFIRMED");
            ticket.setBookingDate(new Date());
            ticket.setUser(user);
            ticket.setTrain(train);

            Ticket savedTicket = ticketRepository.save(ticket);
            log.info("Booking successful");

            return ticketMapper.toResponse(savedTicket);

        } catch (Exception e) {
            log.error("Booking failed: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public BookingResponse getTicket(int ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with ID: " + ticketId));
        return ticketMapper.toResponse(ticket);
    }

    @Override
    public List<BookingResponse> getUserBookings(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        List<Ticket> tickets = ticketRepository.findByUserId(userId);
        return tickets.stream()
                .map(ticketMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelTicket(int ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with ID: " + ticketId));

        Train train = ticket.getTrain();
        if (train != null) {
            train.setAvailableSeats(train.getAvailableSeats() + 1);
            trainRepository.save(train);
        }

        ticketRepository.delete(ticket);
    }
}
