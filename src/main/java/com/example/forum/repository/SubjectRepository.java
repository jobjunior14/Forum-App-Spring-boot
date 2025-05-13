package com.example.forum.repository;

import com.example.forum.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    @Query("SELECT s FROM Subject s WHERE s.title LIKE %:subjectName% AND (:topicId IS NULL OR s.topic.id = :topicId)")
    Page<Subject> findByQueryAndTopic(String subjectName, Long topicId, Pageable pageable);
}