package com.example.forum.service;

import com.example.forum.dto.TopicResponse;
import com.example.forum.entity.Topic;
import com.example.forum.entity.User;
import com.example.forum.exception.ResourceNotFoundException;
import com.example.forum.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TopicService {
    private final TopicRepository topicRepository;

    @Autowired
    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public Page<TopicResponse> findAllTopics(String topicName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Topic> topicPage = topicName == null || topicName.isEmpty() ? topicRepository.findAll(pageable)
                : topicRepository.findAllTopics(topicName, pageable);
        return topicPage.map(topic -> new TopicResponse(
                topic.getId(),
                topic.getName(),
                topic.getUser().getUsername(),
                topic.getUser().getId()));
    }

    public TopicResponse createTopic(String name, User user) {
        Topic topic = new Topic();
        topic.setName(name);
        topic.setUser(user);
        Topic saved = topicRepository.save(topic);
        return new TopicResponse(saved.getId(), saved.getName(), saved.getUser().getUsername(),
                saved.getUser().getId());
    }

    public TopicResponse updateTopic(Long id, String name, User user) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found: " + id));
        if (!topic.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Unauthorized to update topic: " + id);
        }
        topic.setName(name);
        Topic updated = topicRepository.save(topic);
        return new TopicResponse(updated.getId(), updated.getName(), updated.getUser().getUsername(),
                updated.getUser().getId());
    }

    public Topic findById(Long id) {
        return topicRepository.findById(id)
                .orElse(null);
    }

    public void deleteTopic(Long id, User user) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found: " + id));
        if (!topic.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Unauthorized to delete topic: " + id);
        }
        topicRepository.delete(topic);
    }
}