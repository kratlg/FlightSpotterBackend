package com.flightspotter.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpotResponse {
    private UUID id;
    private String icao24;
    private String registration;
    private String aircraftModel;
    private String airline;
    private String imagePath;
    private String imageUrl;
    private String notes;
    private Double spotLatitude;
    private Double spotLongitude;
    private String spotLocation;
    private Double altitude;
    private Double speed;
    private LocalDateTime createdAt;
    private String username;
    private String displayName;
    private UUID userId;
}
