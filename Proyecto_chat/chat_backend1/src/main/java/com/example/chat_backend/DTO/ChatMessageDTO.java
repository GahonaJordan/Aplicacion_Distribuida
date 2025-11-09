package com.example.chat_backend.DTO;

import java.time.LocalDateTime;

public class ChatMessageDTO {
    public enum Type { CHAT, JOIN, LEAVE }

    private Type type;
    private String sender;
    private String content;
    private LocalDateTime timestamp;

    //Constructor por defecto

    public ChatMessageDTO() {
    }

    //Constructor personalizado

    public ChatMessageDTO(Type type, String sender, String content, LocalDateTime timestamp) {
        this.type = type;
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

    //Getters y Setters

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
