package com.example.forum.controller;

import com.example.forum.dto.SubjectRequest;
import com.example.forum.dto.SubjectResponse;
import com.example.forum.entity.User;
import com.example.forum.service.SubjectService;
import com.example.forum.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<Page<SubjectResponse>> getSubjects(
            @RequestParam String subjectName,
            @RequestParam(required = false) Long topicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(subjectService.findSubjects(subjectName, topicId, page, size));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createSubject(
            @ModelAttribute SubjectRequest req,
            @AuthenticationPrincipal User user) {

        if (req == null || req.getTitle() == null) {
            return ResponseEntity.status(401).build();
        }

        String imagePath = null;
        if (req.getImage() != null && !req.getImage().isEmpty()) {
            imagePath = fileStorageService.storeFile(req.getImage());
        }

        subjectService.createSubject(
                req.getTitle(),
                req.getContent(),
                req.getTopicId(),
                imagePath,
                user);

        return ResponseEntity.ok().build();
    }

}