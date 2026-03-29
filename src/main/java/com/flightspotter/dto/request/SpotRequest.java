package com.flightspotter.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SpotRequest {

    @Size(max = 10, message = "ICAO24 code cannot exceed 10 characters")
    private String icao24;

    @Size(max = 20, message = "Registration cannot exceed 20 characters")
    private String registration;

    @Size(max = 100, message = "Aircraft model cannot exceed 100 characters")
    private String aircraftModel;

    @Size(max = 100, message = "Airline cannot exceed 100 characters")
    private String airline;
}
