package com.flightspotter.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlightResponse {

    // OpenSky fields
    private String icao24;
    private String callsign;
    private String originCountry;
    private Long timePosition;
    private Long lastContact;
    private Double longitude;
    private Double latitude;
    private Double baroAltitude;
    private Boolean onGround;
    private Double velocity;      // m/s
    private Double trueTrack;     // heading degrees
    private Double verticalRate;
    private Double geoAltitude;
    private String squawk;

    // Computed fields
    private Double altitudeFeet;  // baroAltitude in feet
    private Double speedKnots;    // velocity in knots
}
