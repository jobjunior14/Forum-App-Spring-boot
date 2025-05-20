package com.example.forum.dto;

public class TopicResponse {
    private Long id;
    private String name;
    private String username;
    private Long userId;

    public TopicResponse(Long id, String name, String username, Long userId) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.userId = userId;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public Long getUserId() {
        return userId;
    }
}