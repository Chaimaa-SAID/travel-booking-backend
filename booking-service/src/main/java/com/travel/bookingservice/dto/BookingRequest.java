package com.travel.bookingservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    private Long flightId;
    private Long hotelId;
    private String paymentMethod;
}
