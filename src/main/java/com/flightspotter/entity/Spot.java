package com.flightspotter.entity;

import com.flightspotter.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "spots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Spot extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "icao24", nullable = false, length = 10)
    private String icao24;

    @Column(name = "registration", length = 20)
    private String registration;

    @Column(name = "aircraft_model", length = 100)
    private String aircraftModel;

    @Column(name = "airline", length = 100)
    private String airline;

    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "spot_latitude")
    private Double spotLatitude;

    @Column(name = "spot_longitude")
    private Double spotLongitude;

    @Column(name = "spot_location", length = 200)
    private String spotLocation;

    @Column(name = "altitude")
    private Double altitude;

    @Column(name = "speed")
    private Double speed;
}
