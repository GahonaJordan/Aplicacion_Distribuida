package com.example.chat_backend.REPOSITORY;

import com.example.chat_backend.MODEL.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
