package com.example.forum.controller;

import com.example.forum.dto.SubjectResponse;
import com.example.forum.entity.User;
import com.example.forum.service.SubjectService;
import com.example.forum.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<Page<SubjectResponse>> getSubjects(
            @RequestParam String query,
            @RequestParam(required = false) Long topicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(subjectService.findSubjects(query, topicId, page, size));
    }

    @PostMapping
    public ResponseEntity<Void> createSubject(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) Long topicId,
            @RequestParam(required = false) MultipartFile image,
            @AuthenticationPrincipal User user) {
        String imagePath = image != null ? fileStorageService.storeFile(image) : null;
        subjectService.createSubject(title, content, topicId, imagePath, user);
        return ResponseEntity.ok().build();
    }
}