package com.travel.paymentservice.controller;

import com.travel.paymentservice.dto.PaymentRequest;
import com.travel.paymentservice.dto.PaymentResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    // USER seulement peut effectuer un paiement
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/pay")
    public PaymentResponse pay(@RequestBody PaymentRequest request) {
        return new PaymentResponse(
                "SUCCESS",
                UUID.randomUUID().toString()
        );
    }
}
