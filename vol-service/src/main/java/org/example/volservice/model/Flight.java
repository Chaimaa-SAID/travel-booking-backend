package org.example.volservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String flightNumber;
    private String departureCity;
    private String destinationCity;

    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;

    private double price;
    private int availableSeats;
}

