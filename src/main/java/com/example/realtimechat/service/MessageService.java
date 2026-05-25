package com.example.realtimechat.service;

import com.example.realtimechat.dto.MessageRequest;
import com.example.realtimechat.dto.MessageResponse;
import com.example.realtimechat.entity.ChatRoom;
import com.example.realtimechat.entity.Message;
import com.example.realtimechat.entity.User;
import com.example.realtimechat.repository.ChatRoomRepository;
import com.example.realtimechat.repository.MessageRepository;
import com.example.realtimechat.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository, ChatRoomRepository chatRoomRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    @Transactional
    public MessageResponse sendMessage(MessageRequest request) {
        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("Sender not found."));

        ChatRoom room = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found."));

        if (!room.getMembers().contains(sender)) {
            room.getMembers().add(sender);
            chatRoomRepository.save(room);
        }

        Message message = new Message(request.getContent(), sender, room);
        return toResponse(messageRepository.save(message));
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> getMessagesByRoom(Long roomId) {
        if (!chatRoomRepository.existsById(roomId)) {
            throw new IllegalArgumentException("Room not found.");
        }

        return messageRepository.findByRoomIdOrderBySentAtAsc(roomId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private MessageResponse toResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getContent(),
                message.getSender().getId(),
                message.getSender().getUsername(),
                message.getRoom().getId(),
                message.getSentAt()
        );
    }
}
