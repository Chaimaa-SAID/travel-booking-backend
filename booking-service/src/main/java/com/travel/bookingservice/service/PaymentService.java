package com.travel.bookingservice.service;

import com.travel.bookingservice.dto.PaymentRequest;
import com.travel.bookingservice.dto.PaymentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PaymentService {

    private final WebClient webClient;
    private final String paymentUrl; // e.g. http://localhost:9000/pay

    public PaymentService(WebClient webClient, @Value("${payment.service.url}") String paymentUrl) {
        this.webClient = webClient;
        this.paymentUrl = paymentUrl;
    }

    public Mono<PaymentResponse> processPayment(PaymentRequest req) {
        // call mock payment endpoint
        return webClient.post()
                .uri(paymentUrl + "/pay")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .onErrorReturn(new PaymentResponse("FAILED", null));
    }
}
