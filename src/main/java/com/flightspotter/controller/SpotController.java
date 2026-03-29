package com.flightspotter.controller;

import com.flightspotter.controller.base.BaseController;
import com.flightspotter.dto.common.ApiResponse;
import com.flightspotter.dto.request.SpotRequest;
import com.flightspotter.dto.response.SpotResponse;
import com.flightspotter.enums.AppMessage;
import com.flightspotter.repository.UserRepository;
import com.flightspotter.security.JwtUtil;
import com.flightspotter.service.SpotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/spots")
@RequiredArgsConstructor
public class SpotController extends BaseController {

    private final SpotService spotService;
    private final UserRepository userRepository;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<SpotResponse>> createSpot(
            @RequestPart("data") @Valid SpotRequest request,
            @RequestPart("image") MultipartFile image,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = getUserId(userDetails);
        return created(AppMessage.SPOT_CREATED, spotService.createSpot(request, image, userId));
    }

    @GetMapping("/feed")
    public ResponseEntity<ApiResponse<Page<SpotResponse>>> getFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok(AppMessage.SPOTS_FOUND, spotService.getFeed(pageable));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<SpotResponse>>> getMySpots(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        UUID userId = getUserId(userDetails);
        Pageable pageable = PageRequest.of(page, size);
        return ok(AppMessage.SPOTS_FOUND, spotService.getUserSpots(userId, pageable));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page<SpotResponse>>> getUserSpots(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok(AppMessage.SPOTS_FOUND, spotService.getUserSpots(userId, pageable));
    }

    @GetMapping("/{spotId}")
    public ResponseEntity<ApiResponse<SpotResponse>> getSpot(@PathVariable UUID spotId) {
        return ok(AppMessage.SPOT_FOUND, spotService.getSpotById(spotId));
    }

    @DeleteMapping("/{spotId}")
    public ResponseEntity<ApiResponse<Void>> deleteSpot(
            @PathVariable UUID spotId,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = getUserId(userDetails);
        spotService.deleteSpot(spotId, userId);
        return ok(AppMessage.SPOT_DELETED);
    }

    private UUID getUserId(UserDetails userDetails) {
        return userRepository.findByEmailAndDeletedFalse(userDetails.getUsername())
                .orElseThrow()
                .getId();
    }
}
