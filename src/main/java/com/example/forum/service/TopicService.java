package com.example.forum.service;

import com.example.forum.entity.Topic;
import com.example.forum.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicService {
    @Autowired
    private TopicRepository topicRepository;

    public Topic findById(Long id) {
        return topicRepository.findById(id)
                .orElse(null);
    }
}