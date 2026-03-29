package com.flightspotter.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AircraftDetailResponse {

    // From hexdb.io
    private String icao24;
    private String registration;
    private String manufacturerName;
    private String model;
    private String registeredOwners;
    private String operator;
    private String type;
    private String icaoTypeCode;
    private String yearBuilt;
    private Integer aircraftAge;

    // Live flight data
    private String callsign;
    private String originCountry;
    private Double latitude;
    private Double longitude;
    private Double baroAltitude;
    private Double altitudeFeet;
    private Double velocity;
    private Double speedKnots;
    private Double trueTrack;
    private Double verticalRate;
    private Boolean onGround;
    private String squawk;

    // Route (from adsbdb.com)
    private Airport origin;
    private Airport destination;

    // Photos from planespotters.net
    private List<AircraftPhoto> photos;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Airport {
        private String icaoCode;
        private String iataCode;
        private String name;
        private String municipality;
        private String countryName;
        private Double latitude;
        private Double longitude;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AircraftPhoto {
        private String thumbnailUrl;
        private String fullUrl;
        private String photographer;
        private String link;
    }
}
