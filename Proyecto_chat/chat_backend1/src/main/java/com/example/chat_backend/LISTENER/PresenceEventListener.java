package com.example.chat_backend.LISTENER;

import com.example.chat_backend.DTO.ChatMessageDTO;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Component
public class PresenceEventListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final Set<String> onlineUsers = new ConcurrentSkipListSet<>();

    public PresenceEventListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        // opcional: notificar broker que hubo conexión
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            onlineUsers.remove(username);
            ChatMessageDTO leaveMessage = new ChatMessageDTO();
            leaveMessage.setType(ChatMessageDTO.Type.LEAVE);
            leaveMessage.setSender(username);
            leaveMessage.setTimestamp(LocalDateTime.now());
            messagingTemplate.convertAndSend("/topic/public", leaveMessage);
            sendOnlineUsers();
        }
    }

    public void userJoined(String username) {
        onlineUsers.add(username);
        ChatMessageDTO joinMessage = new ChatMessageDTO();
        joinMessage.setType(ChatMessageDTO.Type.JOIN);
        joinMessage.setSender(username);
        joinMessage.setTimestamp(LocalDateTime.now());
        messagingTemplate.convertAndSend("/topic/public", joinMessage);
        sendOnlineUsers();
    }

    private void sendOnlineUsers() {
        // idealmente enviar lista específica a otro topic
        messagingTemplate.convertAndSend("/topic/onlineUsers", onlineUsers);
    }

    public Set<String> getOnlineUsers() {
        return onlineUsers;
    }
}
