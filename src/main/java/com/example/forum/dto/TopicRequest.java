package com.example.forum.dto;

public class TopicRequest {
    private String name;

    // Getter & Setter (or use Lombok @Data)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}