package com.example.realtimechat.dto;

public class RoomResponse {

    private Long id;
    private String name;
    private String description;
    private String createdByUsername;
    private int memberCount;

    public RoomResponse(Long id, String name, String description, String createdByUsername, int memberCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdByUsername = createdByUsername;
        this.memberCount = memberCount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public int getMemberCount() {
        return memberCount;
    }
}
