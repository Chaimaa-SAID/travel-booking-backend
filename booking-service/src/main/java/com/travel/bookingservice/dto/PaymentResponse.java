package com.travel.bookingservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private String status; // SUCCESS or FAILED
    private String transactionId;
}
