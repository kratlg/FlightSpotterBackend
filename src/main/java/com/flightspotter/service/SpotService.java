package com.flightspotter.service;

import com.flightspotter.dto.request.SpotRequest;
import com.flightspotter.dto.response.SpotResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface SpotService {
    SpotResponse createSpot(SpotRequest request, MultipartFile image, UUID userId);
    Page<SpotResponse> getUserSpots(UUID userId, Pageable pageable);
    Page<SpotResponse> getFeed(Pageable pageable);
    SpotResponse getSpotById(UUID spotId);
    void deleteSpot(UUID spotId, UUID userId);
}
