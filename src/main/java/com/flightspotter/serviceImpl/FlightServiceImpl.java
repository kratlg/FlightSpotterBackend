package com.flightspotter.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightspotter.dto.response.AircraftDetailResponse;
import com.flightspotter.dto.response.FlightResponse;
import com.flightspotter.enums.AppMessage;
import com.flightspotter.exception.AppException;
import com.flightspotter.service.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String OPENSKY_BASE = "https://opensky-network.org/api";
    private static final String HEXDB_BASE = "https://hexdb.io/api/v1";
    private static final String PLANESPOTTERS_BASE = "https://api.planespotters.net/pub/photos";
    private static final String ADSBDB_BASE = "https://api.adsbdb.com/v0";

    @Override
    public List<FlightResponse> getLiveFlights() {
        return fetchFlightsFromOpenSky(OPENSKY_BASE + "/states/all");
    }

    @Override
    public List<FlightResponse> getLiveFlightsByBounds(double minLat, double maxLat,
                                                        double minLon, double maxLon) {
        String url = String.format(java.util.Locale.US, "%s/states/all?lamin=%.6f&lomin=%.6f&lamax=%.6f&lomax=%.6f",
                OPENSKY_BASE, minLat, minLon, maxLat, maxLon);
        return fetchFlightsFromOpenSky(url);
    }

    @Override
    public AircraftDetailResponse getAircraftDetail(String icao24, String callsign) {
        AircraftDetailResponse.AircraftDetailResponseBuilder builder =
                AircraftDetailResponse.builder().icao24(icao24);

        // Fetch aircraft metadata from hexdb.io
        try {
            String hexUrl = HEXDB_BASE + "/aircraft/" + icao24.toLowerCase();
            ResponseEntity<String> hexResponse = restTemplate.getForEntity(hexUrl, String.class);
            if (hexResponse.getStatusCode().is2xxSuccessful() && hexResponse.getBody() != null) {
                JsonNode node = objectMapper.readTree(hexResponse.getBody());
                builder.registration(getTextOrNull(node, "Registration"))
                       .manufacturerName(getTextOrNull(node, "ManufacturerName"))
                       .model(getTextOrNull(node, "Model"))
                       .registeredOwners(getTextOrNull(node, "RegisteredOwners"))
                       .operator(getTextOrNull(node, "Operator"))
                       .type(getTextOrNull(node, "Type"))
                       .icaoTypeCode(getTextOrNull(node, "ICAOTypeCode"))
                       .yearBuilt(getTextOrNull(node, "YearBuilt"));

                String yearBuilt = getTextOrNull(node, "YearBuilt");
                if (yearBuilt != null && !yearBuilt.isBlank()) {
                    try {
                        int year = Integer.parseInt(yearBuilt.trim());
                        builder.aircraftAge(2025 - year);
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (Exception e) {
            log.warn("Failed to fetch aircraft data from hexdb for {}: {}", icao24, e.getMessage());
        }

        // Fetch photos from planespotters.net
        try {
            String photosUrl = PLANESPOTTERS_BASE + "/hex/" + icao24.toLowerCase();
            ResponseEntity<String> photosResponse = restTemplate.getForEntity(photosUrl, String.class);
            if (photosResponse.getStatusCode().is2xxSuccessful() && photosResponse.getBody() != null) {
                JsonNode root = objectMapper.readTree(photosResponse.getBody());
                JsonNode photos = root.path("photos");
                List<AircraftDetailResponse.AircraftPhoto> photoList = new ArrayList<>();
                if (photos.isArray()) {
                    for (int i = 0; i < Math.min(photos.size(), 5); i++) {
                        JsonNode photo = photos.get(i);
                        JsonNode thumbnail = photo.path("thumbnail_large");
                        JsonNode link = photo.path("link");
                        JsonNode photographer = photo.path("photographer");
                        photoList.add(AircraftDetailResponse.AircraftPhoto.builder()
                                .thumbnailUrl(thumbnail.path("src").asText(null))
                                .fullUrl(thumbnail.path("src").asText(null))
                                .photographer(photographer.asText(null))
                                .link(link.asText(null))
                                .build());
                    }
                }
                builder.photos(photoList);
            }
        } catch (Exception e) {
            log.warn("Failed to fetch photos for {}: {}", icao24, e.getMessage());
        }

        // Fetch route from adsbdb.com (callsign based)
        if (callsign != null && !callsign.isBlank()) {
            try {
                String routeUrl = ADSBDB_BASE + "/callsign/" + callsign.trim();
                ResponseEntity<String> routeResponse = restTemplate.getForEntity(routeUrl, String.class);
                if (routeResponse.getStatusCode().is2xxSuccessful() && routeResponse.getBody() != null) {
                    JsonNode root = objectMapper.readTree(routeResponse.getBody());
                    JsonNode flightroute = root.path("response").path("flightroute");
                    if (!flightroute.isMissingNode()) {
                        JsonNode originNode = flightroute.path("origin");
                        JsonNode destNode = flightroute.path("destination");
                        if (!originNode.isMissingNode()) {
                            builder.origin(AircraftDetailResponse.Airport.builder()
                                    .icaoCode(getTextOrNull(originNode, "icao_code"))
                                    .iataCode(getTextOrNull(originNode, "iata_code"))
                                    .name(getTextOrNull(originNode, "name"))
                                    .municipality(getTextOrNull(originNode, "municipality"))
                                    .countryName(getTextOrNull(originNode, "country_name"))
                                    .latitude(originNode.path("latitude").isNull() ? null : originNode.path("latitude").asDouble())
                                    .longitude(originNode.path("longitude").isNull() ? null : originNode.path("longitude").asDouble())
                                    .build());
                        }
                        if (!destNode.isMissingNode()) {
                            builder.destination(AircraftDetailResponse.Airport.builder()
                                    .icaoCode(getTextOrNull(destNode, "icao_code"))
                                    .iataCode(getTextOrNull(destNode, "iata_code"))
                                    .name(getTextOrNull(destNode, "name"))
                                    .municipality(getTextOrNull(destNode, "municipality"))
                                    .countryName(getTextOrNull(destNode, "country_name"))
                                    .latitude(destNode.path("latitude").isNull() ? null : destNode.path("latitude").asDouble())
                                    .longitude(destNode.path("longitude").isNull() ? null : destNode.path("longitude").asDouble())
                                    .build());
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to fetch route for callsign {}: {}", callsign, e.getMessage());
            }
        }

        return builder.build();
    }

    private List<FlightResponse> fetchFlightsFromOpenSky(String url) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                log.warn("OpenSky returned non-2xx status: {}", response.getStatusCode());
                return new ArrayList<>();
            }

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode states = root.path("states");
            List<FlightResponse> flights = new ArrayList<>();

            if (states.isArray()) {
                for (JsonNode state : states) {
                    if (!state.isArray() || state.size() < 17) continue;
                    Double lat = state.get(6).isNull() ? null : state.get(6).asDouble();
                    Double lon = state.get(5).isNull() ? null : state.get(5).asDouble();
                    if (lat == null || lon == null) continue;

                    Double baroAlt = state.get(7).isNull() ? null : state.get(7).asDouble();
                    Double vel = state.get(9).isNull() ? null : state.get(9).asDouble();

                    flights.add(FlightResponse.builder()
                            .icao24(state.get(0).asText())
                            .callsign(state.get(1).asText("").trim())
                            .originCountry(state.get(2).asText())
                            .timePosition(state.get(3).isNull() ? null : state.get(3).asLong())
                            .lastContact(state.get(4).asLong())
                            .longitude(lon)
                            .latitude(lat)
                            .baroAltitude(baroAlt)
                            .onGround(state.get(8).asBoolean())
                            .velocity(vel)
                            .trueTrack(state.get(10).isNull() ? null : state.get(10).asDouble())
                            .verticalRate(state.get(11).isNull() ? null : state.get(11).asDouble())
                            .geoAltitude(state.get(13).isNull() ? null : state.get(13).asDouble())
                            .squawk(state.get(14).isNull() ? null : state.get(14).asText())
                            .altitudeFeet(baroAlt != null ? baroAlt * 3.28084 : null)
                            .speedKnots(vel != null ? vel * 1.94384 : null)
                            .build());
                }
            }
            log.info("Fetched {} flights from OpenSky", flights.size());
            return flights;
        } catch (Exception e) {
            log.warn("OpenSky API unavailable ({}), returning empty list", e.getMessage());
            return new ArrayList<>();
        }
    }

    private String getTextOrNull(JsonNode node, String field) {
        JsonNode val = node.path(field);
        return val.isNull() || val.isMissingNode() ? null : val.asText(null);
    }
}
