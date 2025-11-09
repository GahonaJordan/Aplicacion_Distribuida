package com.example.chat_backend.SERVICE;

import com.example.chat_backend.MODEL.Message;
import com.example.chat_backend.REPOSITORY.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    private final MessageRepository messageRepository;

    public ChatService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message saveMessage(String sender, String content) {
        // Usar el constructor existente (id = null) para crear la entidad antes de guardar
        Message m = new Message(null, sender, content, LocalDateTime.now());
        return messageRepository.save(m);
    }

    public List<Message> findAll() {
        return messageRepository.findAll();
    }
}
