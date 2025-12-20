package com.travel.bookingservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long flightId;  // stored id of Flight
    private Long hotelId;   // stored id of Hotel

    private LocalDateTime bookingDate;
    private String status; // PENDING, CONFIRMED, CANCELLED

    private double totalPrice;
}
