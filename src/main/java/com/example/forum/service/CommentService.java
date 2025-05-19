package com.example.forum.service;

import com.example.forum.dto.CommentResponse;
import com.example.forum.entity.Comment;
import com.example.forum.entity.Subject;
import com.example.forum.entity.User;
import com.example.forum.exception.ResourceNotFoundException;
import com.example.forum.repository.CommentRepository;
import com.example.forum.repository.SubjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
        private final CommentRepository commentRepository;
        private final SubjectRepository subjectRepository;

        public CommentService(CommentRepository commentRepository, SubjectRepository subjectRepository) {
                this.commentRepository = commentRepository;
                this.subjectRepository = subjectRepository;
        }

        public Page<CommentResponse> findCommentsBySubjectId(Long subjectId, int page, int size) {
                Pageable pageable = PageRequest.of(page, size);
                return commentRepository.findBySubjectId(subjectId, pageable)
                                .map(comment -> new CommentResponse(
                                                comment.getId(),
                                                comment.getContent(),
                                                comment.getUser().getUsername(),
                                                comment.getUser().getId(),
                                                comment.getSubject().getId(),
                                                comment.getParentComment() != null ? comment.getParentComment().getId()
                                                                : null,
                                                comment.getImagePath(),
                                                comment.getCreatedAt()));
        }

        public CommentResponse createComment(String content, Long subjectId, Long parentCommentId, String imagePath,
                        User user) {
                Subject subject = subjectRepository.findById(subjectId)
                                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + subjectId));
                Comment parentComment = parentCommentId != null ? commentRepository.findById(parentCommentId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Parent comment not found: " + parentCommentId))
                                : null;
                Comment comment = new Comment();
                comment.setContent(content);
                comment.setSubject(subject);
                comment.setUser(user);
                comment.setParentComment(parentComment);
                comment.setImagePath(imagePath);
                Comment saved = commentRepository.save(comment);
                return new CommentResponse(
                                saved.getId(),
                                saved.getContent(),
                                saved.getUser().getUsername(),
                                saved.getUser().getId(),
                                saved.getSubject().getId(),
                                saved.getParentComment() != null ? saved.getParentComment().getId() : null,
                                saved.getImagePath(),
                                saved.getCreatedAt());
        }

        public CommentResponse updateComment(Long id, String content, String imagePath, User user) {
                Comment comment = commentRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Comment not found: " + id));
                if (!comment.getUser().getId().equals(user.getId())) {
                        throw new ResourceNotFoundException("Unauthorized to update comment: " + id);
                }
                comment.setContent(content);
                if (imagePath != null) {
                        comment.setImagePath(imagePath);
                }
                Comment updated = commentRepository.save(comment);
                return new CommentResponse(
                                updated.getId(),
                                updated.getContent(),
                                updated.getUser().getUsername(),
                                updated.getUser().getId(),
                                updated.getSubject().getId(),
                                updated.getParentComment() != null ? updated.getParentComment().getId() : null,
                                updated.getImagePath(),
                                updated.getCreatedAt());
        }

        public void deleteComment(Long id, User user) {
                Comment comment = commentRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Comment not found: " + id));
                if (!comment.getUser().getId().equals(user.getId())) {
                        throw new ResourceNotFoundException("Unauthorized to delete comment: " + id);
                }
                commentRepository.delete(comment);
        }
}