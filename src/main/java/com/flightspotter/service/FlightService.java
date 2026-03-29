package com.flightspotter.service;

import com.flightspotter.dto.response.AircraftDetailResponse;
import com.flightspotter.dto.response.FlightResponse;

import java.util.List;

public interface FlightService {
    List<FlightResponse> getLiveFlights();
    List<FlightResponse> getLiveFlightsByBounds(double minLat, double maxLat, double minLon, double maxLon);
    AircraftDetailResponse getAircraftDetail(String icao24, String callsign);
}
