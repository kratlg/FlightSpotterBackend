package com.flightspotter.repository;

import com.flightspotter.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    List<Comment> findBySpotIdAndDeletedFalseOrderByCreatedAtAsc(UUID spotId);

    Optional<Comment> findByIdAndDeletedFalse(UUID id);
}
