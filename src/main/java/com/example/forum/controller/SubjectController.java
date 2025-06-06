package com.example.forum.controller;

import com.example.forum.config.UserDetailsImpl;
import com.example.forum.dto.SubjectRequest;
import com.example.forum.dto.SubjectResponse;
import com.example.forum.entity.User;
import com.example.forum.entity.Subject;
import com.example.forum.service.SubjectService;
import com.example.forum.service.FileStorageService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    private final SubjectService subjectService;
    private final FileStorageService fileStorageService;

    public SubjectController(SubjectService subjectService, FileStorageService fileStorageService) {
        this.subjectService = subjectService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public ResponseEntity<Page<SubjectResponse>> getSubjects(
            @RequestParam String subjectName,
            @RequestParam(required = false) Long topicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(subjectService.findSubjects(subjectName, topicId, page, size));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> createSubject(@ModelAttribute SubjectRequest req) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        if (req == null || req.getTitle() == null) {
            return ResponseEntity.status(401).body("Vous devrez etre connecter");
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

    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SubjectResponse> updateSubject(@ModelAttribute SubjectRequest req, @PathVariable Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        if (id == null) {
            return ResponseEntity.status(401).body(null);
        }

        String imagePath = req.getImage() != null ? fileStorageService.storeFile(req.getImage()) : null;
        Subject subject = subjectService.updateSubject(id, req.getTitle(), req.getContent(), req.getTopicId(),
                imagePath, user);
        SubjectResponse response = new SubjectResponse(subject.getId(), subject.getTitle(), subject.getContent(),
                subject.getUser().getUsername(), subject.getTopic() != null ? subject.getTopic().getName() : null,
                subject.getImagePath(), subject.getCreatedAt());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        User user = userDetails.getUser();
        subjectService.deleteSubject(id, user);
        return ResponseEntity.ok().build();
    }
}