package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Ticket;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IRCTCTicketRepository extends JpaRepository<Ticket, Integer>{

	@Query("SELECT t FROM Ticket t WHERE t.user.id = :userId")
	List<Ticket> findByUserId(@Param("userId") int userId);

	boolean existsByTrain_TrainNumber(int trainNumber);

	boolean existsByUser_Id(int userId);
}
