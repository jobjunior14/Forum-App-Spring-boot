package com.example.forum.controller;

import com.example.forum.dto.CommentResponse;
import com.example.forum.entity.User;
import com.example.forum.service.CommentService;
import com.example.forum.service.FileStorageService;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
// import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;
    private final FileStorageService fileStorageService;

    // @Autowired
    public CommentController(CommentService commentService, FileStorageService fileStorageService) {
        this.commentService = commentService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/{subjectId}")
    public ResponseEntity<Page<CommentResponse>> getComments(
            @PathVariable Long subjectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.findCommentsBySubjectId(subjectId, page, size));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentResponse> createComment(
            @RequestParam String content,
            @RequestParam Long subjectId,
            @RequestParam(required = false) Long parentCommentId,
            @RequestParam(required = false) MultipartFile image,
            @AuthenticationPrincipal User user) {
        String imagePath = image != null && !image.isEmpty() ? fileStorageService.storeFile(image) : null;
        CommentResponse comment = commentService.createComment(content, subjectId, parentCommentId, imagePath, user);
        return ResponseEntity.ok(comment);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long id,
            @RequestParam String content,
            @RequestParam(required = false) MultipartFile image,
            @AuthenticationPrincipal User user) {
        String imagePath = image != null && !image.isEmpty() ? fileStorageService.storeFile(image) : null;
        CommentResponse comment = commentService.updateComment(id, content, imagePath, user);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        commentService.deleteComment(id, user);
        return ResponseEntity.ok().build();
    }
}