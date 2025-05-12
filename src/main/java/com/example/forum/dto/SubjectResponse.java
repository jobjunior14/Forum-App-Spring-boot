package com.example.forum.dto;

import java.time.LocalDateTime;

public class SubjectResponse {
    private Long id;
    private String title;
    private String content;
    private String username;
    private String topicName;
    private String imagePath;
    private LocalDateTime createdAt;

    public SubjectResponse(Long id, String title, String content, String username, String topicName, String imagePath,
            LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
        this.topicName = topicName;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUsername() {
        return username;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}