package com.example.forum.controller;

import com.example.forum.dto.CommentResponse;
import com.example.forum.entity.User;
import com.example.forum.service.CommentService;
import com.example.forum.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/{subjectId}")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long subjectId) {
        return ResponseEntity.ok(commentService.findCommentsBySubjectId(subjectId));
    }

    @PostMapping
    public ResponseEntity<Void> createComment(
            @RequestParam String content,
            @RequestParam Long subjectId,
            @RequestParam(required = false) Long parentCommentId,
            @RequestParam(required = false) MultipartFile image,
            @AuthenticationPrincipal User user) {
        String imagePath = image != null ? fileStorageService.storeFile(image) : null;
        commentService.createComment(content, subjectId, parentCommentId, imagePath, user);
        return ResponseEntity.ok().build();
    }
}