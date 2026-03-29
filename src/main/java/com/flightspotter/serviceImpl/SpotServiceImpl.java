package com.flightspotter.serviceImpl;

import com.flightspotter.dto.request.SpotRequest;
import com.flightspotter.dto.response.SpotResponse;
import com.flightspotter.entity.Spot;
import com.flightspotter.entity.User;
import com.flightspotter.enums.AppMessage;
import com.flightspotter.exception.AppException;
import com.flightspotter.repository.SpotRepository;
import com.flightspotter.repository.UserRepository;
import com.flightspotter.service.SpotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpotServiceImpl implements SpotService {

    private final SpotRepository spotRepository;
    private final UserRepository userRepository;

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    private static final List<String> ALLOWED_TYPES = List.of(
            "image/jpeg", "image/png", "image/webp", "image/jpg"
    );

    @Override
    @Transactional
    public SpotResponse createSpot(SpotRequest request, MultipartFile image, UUID userId) {
        if (image == null || image.isEmpty()) {
            throw AppException.badRequest(AppMessage.FILE_UPLOAD_ERROR);
        }
        if (!ALLOWED_TYPES.contains(image.getContentType())) {
            throw AppException.badRequest(AppMessage.FILE_TYPE_INVALID);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> AppException.notFound(AppMessage.USER_NOT_FOUND));

        String imagePath = saveImage(image, userId);

        String icao24 = (request.getIcao24() != null && !request.getIcao24().isBlank())
                ? request.getIcao24() : "UNKNOWN";

        Spot spot = Spot.builder()
                .user(user)
                .icao24(icao24)
                .registration(request.getRegistration())
                .aircraftModel(request.getAircraftModel())
                .airline(request.getAirline())
                .imagePath(imagePath)
                .build();

        spot = spotRepository.save(spot);
        return toResponse(spot);
    }

    @Override
    public Page<SpotResponse> getUserSpots(UUID userId, Pageable pageable) {
        return spotRepository
                .findByUserIdAndDeletedFalseOrderByCreatedAtDesc(userId, pageable)
                .map(this::toResponse);
    }

    @Override
    public Page<SpotResponse> getFeed(Pageable pageable) {
        return spotRepository
                .findByDeletedFalseOrderByCreatedAtDesc(pageable)
                .map(this::toResponse);
    }

    @Override
    public SpotResponse getSpotById(UUID spotId) {
        Spot spot = spotRepository.findByIdAndDeletedFalse(spotId)
                .orElseThrow(() -> AppException.notFound(AppMessage.SPOT_NOT_FOUND));
        return toResponse(spot);
    }

    @Override
    @Transactional
    public void deleteSpot(UUID spotId, UUID userId) {
        Spot spot = spotRepository.findByIdAndDeletedFalse(spotId)
                .orElseThrow(() -> AppException.notFound(AppMessage.SPOT_NOT_FOUND));
        if (!spot.getUser().getId().equals(userId)) {
            throw AppException.forbidden(AppMessage.SPOT_NOT_AUTHORIZED);
        }
        spot.setDeleted(true);
        spotRepository.save(spot);
    }

    private String saveImage(MultipartFile file, UUID userId) {
        try {
            Path uploadPath = Paths.get(uploadDir, "spots", userId.toString());
            Files.createDirectories(uploadPath);

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);
            file.transferTo(filePath);

            return "spots/" + userId + "/" + filename;
        } catch (IOException e) {
            log.error("Failed to save image: {}", e.getMessage());
            throw AppException.internalError(AppMessage.FILE_UPLOAD_ERROR);
        }
    }

    private SpotResponse toResponse(Spot spot) {
        return SpotResponse.builder()
                .id(spot.getId())
                .icao24(spot.getIcao24())
                .registration(spot.getRegistration())
                .aircraftModel(spot.getAircraftModel())
                .airline(spot.getAirline())
                .imagePath(spot.getImagePath())
                .imageUrl("/uploads/" + spot.getImagePath())
                .notes(spot.getNotes())
                .spotLatitude(spot.getSpotLatitude())
                .spotLongitude(spot.getSpotLongitude())
                .spotLocation(spot.getSpotLocation())
                .altitude(spot.getAltitude())
                .speed(spot.getSpeed())
                .createdAt(spot.getCreatedAt())
                .username(spot.getUser().getUsername())
                .displayName(spot.getUser().getDisplayName())
                .userId(spot.getUser().getId())
                .build();
    }
}
