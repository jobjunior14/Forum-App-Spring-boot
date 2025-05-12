package com.example.forum.service;

import com.example.forum.entity.Comment;
import com.example.forum.entity.Subject;
import com.example.forum.entity.User;
import com.example.forum.repository.CommentRepository;
import com.example.forum.repository.SubjectRepository;
import com.example.forum.dto.CommentResponse;
import com.example.forum.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    public List<CommentResponse> findCommentsBySubjectId(Long subjectId) {
        return commentRepository.findBySubjectId(subjectId).stream()
                .map(comment -> new CommentResponse(comment.getId(), comment.getContent(),
                        comment.getUser().getUsername(),
                        comment.getSubject().getId(),
                        comment.getParentComment() != null ? comment.getParentComment().getId() : null,
                        comment.getImagePath(), comment.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public Comment createComment(String content, Long subjectId, Long parentCommentId, String imagePath, User user) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + subjectId));
        Comment parentComment = parentCommentId != null ? commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found: " + parentCommentId))
                : null;
        Comment comment = new Comment(content, user, subject, parentComment, imagePath);
        return commentRepository.save(comment);
    }
}