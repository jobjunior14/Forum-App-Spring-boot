package com.example.forum.dto;

import java.time.LocalDateTime;

public class CommentResponse {
    private Long id;
    private String content;
    private String username;
    private Long userId;
    private Long subjectId;
    private Long parentCommentId;
    private String imagePath;
    private LocalDateTime createdAt;

    public CommentResponse(Long id, String content, String username, Long userId, Long subjectId, Long parentCommentId,
            String imagePath, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.username = username;
        this.userId = userId;
        this.subjectId = subjectId;
        this.parentCommentId = parentCommentId;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getUsername() {
        return username;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}