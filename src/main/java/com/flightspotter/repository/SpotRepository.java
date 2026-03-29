package com.flightspotter.repository;

import com.flightspotter.entity.Spot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpotRepository extends JpaRepository<Spot, UUID> {

    Page<Spot> findByUserIdAndDeletedFalseOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    List<Spot> findByIcao24AndDeletedFalse(String icao24);

    Optional<Spot> findByIdAndDeletedFalse(UUID id);

    Page<Spot> findByDeletedFalseOrderByCreatedAtDesc(Pageable pageable);

    long countByUserIdAndDeletedFalse(UUID userId);
}
