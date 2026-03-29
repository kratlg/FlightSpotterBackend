package com.flightspotter.controller;

import com.flightspotter.controller.base.BaseController;
import com.flightspotter.dto.common.ApiResponse;
import com.flightspotter.dto.response.AircraftDetailResponse;
import com.flightspotter.dto.response.FlightResponse;
import com.flightspotter.enums.AppMessage;
import com.flightspotter.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController extends BaseController {

    private final FlightService flightService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<FlightResponse>>> getLiveFlights() {
        return ok(AppMessage.FLIGHTS_FETCHED, flightService.getLiveFlights());
    }

    @GetMapping("/bounds")
    public ResponseEntity<ApiResponse<List<FlightResponse>>> getFlightsByBounds(
            @RequestParam double minLat,
            @RequestParam double maxLat,
            @RequestParam double minLon,
            @RequestParam double maxLon) {
        return ok(AppMessage.FLIGHTS_FETCHED,
                flightService.getLiveFlightsByBounds(minLat, maxLat, minLon, maxLon));
    }

    @GetMapping("/{icao24}")
    public ResponseEntity<ApiResponse<AircraftDetailResponse>> getAircraftDetail(
            @PathVariable String icao24,
            @RequestParam(required = false, defaultValue = "") String callsign) {
        return ok(AppMessage.FLIGHT_DETAIL_FETCHED, flightService.getAircraftDetail(icao24, callsign));
    }
}
