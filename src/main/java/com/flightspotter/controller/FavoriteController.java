package com.flightspotter.controller;

import com.flightspotter.controller.base.BaseController;
import com.flightspotter.dto.common.ApiResponse;
import com.flightspotter.enums.AppMessage;
import com.flightspotter.entity.Favorite;
import com.flightspotter.repository.FavoriteRepository;
import com.flightspotter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController extends BaseController {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<String>>> getFavorites(
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = getUserId(userDetails);
        List<String> icaos = favoriteRepository.findByUserId(userId)
                .stream()
                .map(Favorite::getIcao24)
                .collect(Collectors.toList());
        return ok(AppMessage.FAVORITES_FOUND, icaos);
    }

    @PostMapping("/{icao24}")
    public ResponseEntity<ApiResponse<Void>> addFavorite(
            @PathVariable String icao24,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = getUserId(userDetails);
        if (!favoriteRepository.existsByUserIdAndIcao24(userId, icao24.toLowerCase())) {
            favoriteRepository.save(Favorite.builder()
                    .userId(userId)
                    .icao24(icao24.toLowerCase())
                    .build());
        }
        return ok(AppMessage.FAVORITE_ADDED);
    }

    @DeleteMapping("/{icao24}")
    public ResponseEntity<ApiResponse<Void>> removeFavorite(
            @PathVariable String icao24,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = getUserId(userDetails);
        favoriteRepository.findByUserIdAndIcao24(userId, icao24.toLowerCase())
                .ifPresent(favoriteRepository::delete);
        return ok(AppMessage.FAVORITE_REMOVED);
    }

    private UUID getUserId(UserDetails userDetails) {
        return userRepository.findByEmailAndDeletedFalse(userDetails.getUsername())
                .orElseThrow()
                .getId();
    }
}
