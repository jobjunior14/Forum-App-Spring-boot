package com.example.forum.controller;

import com.example.forum.config.UserDetailsImpl;
import com.example.forum.dto.CommentRequest;
import com.example.forum.dto.CommentResponse;
import com.example.forum.entity.User;
import com.example.forum.service.CommentService;
import com.example.forum.service.FileStorageService;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentResponse> createComment(@ModelAttribute CommentRequest req) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        String imagePath = null;

        if (req.getImage() != null && !req.getImage().isEmpty()) {
            imagePath = fileStorageService.storeFile(req.getImage());
        }

        CommentResponse comment = commentService.createComment(req.getContent(), req.getSubjectId(),
                req.getParentCommentId(), imagePath, user);
        return ResponseEntity.ok(comment);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long id, @ModelAttribute CommentRequest req) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        String imagePath = null;

        if (req.getImage() != null && !req.getImage().isEmpty()) {
            imagePath = fileStorageService.storeFile(req.getImage());
        }

        CommentResponse comment = commentService.updateComment(id, req.getContent(), imagePath, user);
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