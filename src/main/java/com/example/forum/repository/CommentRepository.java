package com.example.forum.repository;

import com.example.forum.entity.Comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
// import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // List<Comment> findBySubjectId(Long subjectId);
    @Query("SELECT c FROM Comment c WHERE c.subject.id = :subjectId")
    Page<Comment> findBySubjectId(Long subjectId, Pageable pageable);
}