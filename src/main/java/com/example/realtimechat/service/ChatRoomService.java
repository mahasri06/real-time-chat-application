package com.example.realtimechat.service;

import com.example.realtimechat.dto.CreateRoomRequest;
import com.example.realtimechat.dto.RoomResponse;
import com.example.realtimechat.entity.ChatRoom;
import com.example.realtimechat.entity.User;
import com.example.realtimechat.repository.ChatRoomRepository;
import com.example.realtimechat.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository, UserRepository userRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public RoomResponse createRoom(CreateRoomRequest request) {
        if (chatRoomRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("A room with this name already exists.");
        }

        User creator = findUser(request.getCreatedByUserId());
        ChatRoom room = new ChatRoom(request.getName(), request.getDescription(), creator);
        return toResponse(chatRoomRepository.save(room));
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getAllRooms() {
        return chatRoomRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public RoomResponse joinRoom(Long roomId, Long userId) {
        ChatRoom room = findRoom(roomId);
        User user = findUser(userId);
        room.getMembers().add(user);
        return toResponse(chatRoomRepository.save(room));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    private ChatRoom findRoom(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found."));
    }

    private RoomResponse toResponse(ChatRoom room) {
        return new RoomResponse(
                room.getId(),
                room.getName(),
                room.getDescription(),
                room.getCreatedBy().getUsername(),
                room.getMembers().size()
        );
    }
}
