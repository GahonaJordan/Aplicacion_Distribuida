package com.example.chat_backend.CONTROLLER;

import com.example.chat_backend.DTO.ChatMessageDTO;
import com.example.chat_backend.SERVICE.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    public WebSocketController(SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessageDTO chatMessage) {
        // agregar timestamp si falta
        if (chatMessage.getTimestamp() == null) {
            chatMessage.setTimestamp(LocalDateTime.now());
        }
        // persistir solo mensajes tipo CHAT
        if (chatMessage.getType() == ChatMessageDTO.Type.CHAT) {
            chatService.saveMessage(chatMessage.getSender(), chatMessage.getContent());
        }
        // reenviar a todos los subscriptores de /topic/public
        messagingTemplate.convertAndSend("/topic/public", chatMessage);
    }

    @MessageMapping("/chat.addUser")
    public void addUser(ChatMessageDTO chatMessage) {
        chatMessage.setType(ChatMessageDTO.Type.JOIN);
        chatMessage.setTimestamp(LocalDateTime.now());
        messagingTemplate.convertAndSend("/topic/public", chatMessage);
    }
}
