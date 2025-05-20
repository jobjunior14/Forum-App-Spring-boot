package com.example.forum.repository;

import com.example.forum.entity.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    @Query("SELECT t FROM Topic t WHERE :name IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Topic> findAllTopics(@Param("name") String name, Pageable pageable);
}