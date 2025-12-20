package com.travel.bookingservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    private Long userId;
    private Long flightId;
    private Long hotelId;
    private String paymentMethod; // e.g. "CARD"
}
