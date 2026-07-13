package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.dto.request.BookingRequest;
import com.example.demo.dto.response.BookingResponse;
import com.example.demo.entity.Ticket;
import com.example.demo.entity.Train;
import com.example.demo.entity.User;
import com.example.demo.exception.SeatNotAvailableException;
import com.example.demo.exception.TrainNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.mapper.TicketMapper;
import com.example.demo.mapper.TrainMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.IRCTCTicketRepository;
import com.example.demo.repository.IRCTCTrainRepository;
import com.example.demo.repository.IRCTCUserRepository;
import com.example.demo.service.impl.IRCTCServiceImpl;

@ExtendWith(MockitoExtension.class)
public class IRCTCServiceImplTest {

    @Mock
    private IRCTCUserRepository userRepository;

    @Mock
    private IRCTCTrainRepository trainRepository;

    @Mock
    private IRCTCTicketRepository ticketRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private TrainMapper trainMapper;

    @Mock
    private TicketMapper ticketMapper;

    private IRCTCService irctcService;

    @BeforeEach
    public void setup() {
        irctcService = new IRCTCServiceImpl(
            userRepository,
            trainRepository,
            ticketRepository,
            userMapper,
            trainMapper,
            ticketMapper
        );
    }

    @Test
    public void testBookTicketUserNotFound() {
        BookingRequest request = new BookingRequest();
        request.setUserId(99);
        request.setTrainNumber(12001);

        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            irctcService.bookTicket(request);
        });
    }

    @Test
    public void testBookTicketTrainNotFound() {
        BookingRequest request = new BookingRequest();
        request.setUserId(1);
        request.setTrainNumber(99999);

        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(trainRepository.findByTrainNumber(99999)).thenReturn(Optional.empty());

        assertThrows(TrainNotFoundException.class, () -> {
            irctcService.bookTicket(request);
        });
    }

    @Test
    public void testBookTicketNoSeatsAvailable() {
        BookingRequest request = new BookingRequest();
        request.setUserId(1);
        request.setTrainNumber(12127);

        User user = new User();
        user.setId(1);

        Train train = new Train();
        train.setTrainNumber(12127);
        train.setAvailableSeats(0); 

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(trainRepository.findByTrainNumber(12127)).thenReturn(Optional.of(train));

        assertThrows(SeatNotAvailableException.class, () -> {
            irctcService.bookTicket(request);
        });
    }

    @Test
    public void testBookTicketSuccess() {
        BookingRequest request = new BookingRequest();
        request.setUserId(1);
        request.setTrainNumber(12124);

        User user = new User();
        user.setId(1);
        user.setName("John Doe");

        Train train = new Train();
        train.setTrainNumber(12124);
        train.setSource("Pune");
        train.setDestination("Mumbai");
        train.setAvailableSeats(10);

        Ticket savedTicket = new Ticket();
        savedTicket.setTicketId(101);
        savedTicket.setTrainNumber(12124);
        savedTicket.setSource("Pune");
        savedTicket.setDestination("Mumbai");
        savedTicket.setSeatNumber(10);
        savedTicket.setStatus("CONFIRMED");
        savedTicket.setUser(user);
        savedTicket.setTrain(train);

        BookingResponse mockResponse = new BookingResponse();
        mockResponse.setTicketId(101);
        mockResponse.setTrainNumber(12124);
        mockResponse.setSource("Pune");
        mockResponse.setDestination("Mumbai");
        mockResponse.setSeatNumber(10);
        mockResponse.setStatus("CONFIRMED");
        mockResponse.setUserId(1);
        mockResponse.setUserName("John Doe");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(trainRepository.findByTrainNumber(12124)).thenReturn(Optional.of(train));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(savedTicket);
        when(ticketMapper.toResponse(any(Ticket.class))).thenReturn(mockResponse);

        BookingResponse response = irctcService.bookTicket(request);

        assertEquals(101, response.getTicketId());
        assertEquals(9, train.getAvailableSeats()); 
        assertEquals(12124, response.getTrainNumber());
        assertEquals("CONFIRMED", response.getStatus());

        verify(trainRepository, times(1)).save(train);
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }
}
