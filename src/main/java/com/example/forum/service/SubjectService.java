package com.example.forum.service;

import com.example.forum.entity.Subject;
import com.example.forum.entity.User;
import com.example.forum.entity.Topic;
import com.example.forum.repository.SubjectRepository;
import com.example.forum.dto.SubjectResponse;
import com.example.forum.exception.ResourceNotFoundException;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final TopicService topicService;
    private final FileStorageService fileStorageService;

    public SubjectService(SubjectRepository subjectRepository, TopicService topicService,
            FileStorageService fileStorageService) {
        this.subjectRepository = subjectRepository;
        this.topicService = topicService;
        this.fileStorageService = fileStorageService;
    }

    public Page<SubjectResponse> findSubjects(String query, Long topicId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return subjectRepository.findByQueryAndTopic(query, topicId, pageable)
                .map(subject -> new SubjectResponse(subject.getId(), subject.getTitle(), subject.getContent(),
                        subject.getUser().getUsername(),
                        subject.getTopic() != null ? subject.getTopic().getName() : null,
                        subject.getImagePath(), subject.getCreatedAt()));
    }

    public Subject createSubject(String title, String content, Long topicId, String imagePath, User user) {
        Topic topic = topicId != null ? topicService.findById(topicId) : null;
        Subject subject = new Subject(title, content, user, topic, imagePath);
        return subjectRepository.save(subject);
    }

    public Subject updateSubject(Long id, String title, String content, Long topicId, String imagePath, User user) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + id));
        if (!subject.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Unauthorized to update subject: " + id);
        }
        subject.setTitle(title);
        subject.setContent(content);
        subject.setTopic(topicId != null ? topicService.findById(topicId) : null);
        subject.setImagePath(imagePath);
        return subjectRepository.save(subject);
    }

    public void deleteSubject(Long id, User user) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + id));
        if (!subject.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Unauthorized to delete subject: " + id);
        }
        subjectRepository.delete(subject);
    }
}