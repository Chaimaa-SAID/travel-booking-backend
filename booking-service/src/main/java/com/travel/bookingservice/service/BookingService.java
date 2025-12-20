package com.travel.bookingservice.service;


import com.travel.bookingservice.client.FlightClient;
import com.travel.bookingservice.client.HotelClient;
import com.travel.bookingservice.dto.*;
import com.travel.bookingservice.model.Booking;
import com.travel.bookingservice.model.Payment;
import com.travel.bookingservice.repository.BookingRepository;
import com.travel.bookingservice.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final FlightClient flightClient;
    private final HotelClient hotelClient;
    private final PaymentService paymentService;

    public BookingService(BookingRepository bookingRepository,
                          PaymentRepository paymentRepository,
                          FlightClient flightClient,
                          HotelClient hotelClient,
                          PaymentService paymentService) {
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.flightClient = flightClient;
        this.hotelClient = hotelClient;
        this.paymentService = paymentService;
    }

    public Mono<BookingResponse> createBooking(BookingRequest req) {
        // 1. fetch flight & hotel data (Feign calls are blocking by default)
        var flight = flightClient.getFlightById(req.getFlightId());
        var hotel = hotelClient.getHotelById(req.getHotelId());

        // 2. calculate total price
        double total = flight.price + hotel.pricePerNight;

        // 3. create booking in PENDING state
        Booking booking = Booking.builder()
                .userId(req.getUserId())
                .flightId(req.getFlightId())
                .hotelId(req.getHotelId())
                .bookingDate(LocalDateTime.now())
                .status("PENDING")
                .totalPrice(total)
                .build();

        Booking saved = bookingRepository.save(booking);

        // 4. call payment (reactive)
        PaymentRequest paymentReq = new PaymentRequest(total, req.getPaymentMethod());
        return paymentService.processPayment(paymentReq)
                .map(pr -> {
                    if ("SUCCESS".equalsIgnoreCase(pr.getStatus())) {
                        // save payment record
                        Payment p = Payment.builder()
                                .paymentMethod(req.getPaymentMethod())
                                .amount(total)
                                .status("SUCCESS")
                                .paymentDate(LocalDateTime.now())
                                .transactionId(pr.getTransactionId())
                                .build();
                        paymentRepository.save(p);

                        // update booking
                        saved.setStatus("CONFIRMED");
                        bookingRepository.save(saved);

                        return new BookingResponse(saved.getId(), "CONFIRMED", total);
                    } else {
                        saved.setStatus("FAILED");
                        bookingRepository.save(saved);
                        return new BookingResponse(saved.getId(), "FAILED", total);
                    }
                });
    }

    public Booking getBooking(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }
    public void saveBooking(Booking booking) {
        bookingRepository.save(booking);
    }
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

}
