package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Ticket;
import com.example.demo.entity.Train;

public interface IRCTCTrainRepository extends JpaRepository<Train, Integer>{

	List<Train> findBySourceAndDestination(String source, String destination);
	Optional<Train> findByTrainNumber(int trainNumber);
	boolean existsByTrainNumber(int trainNumber);
}
