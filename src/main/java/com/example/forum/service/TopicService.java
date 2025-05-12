package com.example.forum.service;

import com.example.forum.entity.Topic;
import com.example.forum.exception.ResourceNotFoundException;
import com.example.forum.repository.TopicRepository;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TopicService {
    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public Topic findById(Long id) {
        return topicRepository.findById(id)
                .orElse(null);
    }

    public Topic createTopic(String name) {
        Topic topic = new Topic(name);
        return topicRepository.save(topic);
    }

    public List<Topic> findAllTopics() {
        return topicRepository.findAll();
    }

    public Topic updateTopic(Long id, String name) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found: " + id));
        topic.setName(name);
        return topicRepository.save(topic);
    }

    public void deleteTopic(Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found: " + id));
        topicRepository.delete(topic);
    }
}