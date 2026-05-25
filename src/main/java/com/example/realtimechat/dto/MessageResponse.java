package com.example.realtimechat.dto;

import java.time.LocalDateTime;

public class MessageResponse {

    private Long id;
    private String content;
    private Long senderId;
    private String senderUsername;
    private Long roomId;
    private LocalDateTime sentAt;

    public MessageResponse(Long id, String content, Long senderId, String senderUsername, Long roomId, LocalDateTime sentAt) {
        this.id = id;
        this.content = content;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.roomId = roomId;
        this.sentAt = sentAt;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public Long getRoomId() {
        return roomId;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }
}
