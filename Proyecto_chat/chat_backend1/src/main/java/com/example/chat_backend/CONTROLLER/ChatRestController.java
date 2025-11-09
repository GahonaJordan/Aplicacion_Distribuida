package com.example.chat_backend.CONTROLLER;

import com.example.chat_backend.LISTENER.PresenceEventListener;
import com.example.chat_backend.MODEL.Message;
import com.example.chat_backend.SERVICE.ChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    private final ChatService chatService;
    private final PresenceEventListener presence;

    public ChatRestController(ChatService chatService, PresenceEventListener presence) {
        this.chatService = chatService;
        this.presence = presence;
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return chatService.findAll();
    }

    @GetMapping("/online")
    public Set<String> getOnlineUsers() {
        return presence.getOnlineUsers();
    }
}
