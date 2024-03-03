package com.example.libraryconsumer.repository;

import com.example.libraryconsumer.entity.LibraryEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryEventsRepository extends JpaRepository<LibraryEvent, Integer> {

}
