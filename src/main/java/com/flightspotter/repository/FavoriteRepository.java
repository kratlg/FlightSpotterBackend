package com.flightspotter.repository;

import com.flightspotter.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {

    List<Favorite> findByUserId(UUID userId);

    Optional<Favorite> findByUserIdAndIcao24(UUID userId, String icao24);

    boolean existsByUserIdAndIcao24(UUID userId, String icao24);
}
