package com.travel.bookingservice.dto;

import lombok.*;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long bookingId;
    private String status;
    private double totalPrice;
    private String type; // "FLIGHT" ou "HOTEL"
    private Map<String, Object> itemDetails; // détails du vol ou de l’hôtel
}
