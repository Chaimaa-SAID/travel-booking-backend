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
import java.util.HashMap;
import java.util.Map;
import java.util.List;

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
        // 1. récupérer flight & hotel via Feign clients
        FlightDto flight = flightClient.getFlightById(req.getFlightId());
        HotelDto hotel = hotelClient.getHotelById(req.getHotelId());

        // 2. calculer le total
        double total = (flight != null ? flight.price : 0) + (hotel != null ? hotel.pricePerNight : 0);

        // 3. créer la réservation en PENDING
        Booking booking = Booking.builder()
                .flightId(req.getFlightId())
                .hotelId(req.getHotelId())
                .bookingDate(LocalDateTime.now())
                .status("PENDING")
                .totalPrice(total)
                .build();

        Booking saved = bookingRepository.save(booking);

        // 4. préparer itemDetails
        Map<String, Object> itemDetails = new HashMap<>();
        if (flight != null) {
            itemDetails.put("flightNumber", flight.flightNumber);
            itemDetails.put("departureCity", flight.departureCity);
            itemDetails.put("destinationCity", flight.destinationCity);
            itemDetails.put("departureDate", flight.departureDate);
            itemDetails.put("arrivalDate", flight.arrivalDate);
        }
        if (hotel != null) {
            itemDetails.put("hotelName", hotel.name);
            itemDetails.put("hotelCity", hotel.city);
            itemDetails.put("hotelAddress", hotel.address);
            itemDetails.put("pricePerNight", hotel.pricePerNight);
        }

        // 5. appel au service de paiement
        PaymentRequest paymentReq = new PaymentRequest(total, req.getPaymentMethod());
        return paymentService.processPayment(paymentReq)
                .map(pr -> {
                    if ("SUCCESS".equalsIgnoreCase(pr.getStatus())) {
                        // sauvegarder le paiement
                        Payment p = Payment.builder()
                                .paymentMethod(req.getPaymentMethod())
                                .amount(total)
                                .status("SUCCESS")
                                .paymentDate(LocalDateTime.now())
                                .transactionId(pr.getTransactionId())
                                .build();
                        paymentRepository.save(p);

                        // mettre à jour la réservation
                        saved.setStatus("CONFIRMED");
                        bookingRepository.save(saved);

                        return new BookingResponse(
                                saved.getId(),
                                "CONFIRMED",
                                total,
                                "FLIGHT_HOTEL",
                                itemDetails
                        );
                    } else {
                        saved.setStatus("FAILED");
                        bookingRepository.save(saved);

                        return new BookingResponse(
                                saved.getId(),
                                "FAILED",
                                total,
                                "FLIGHT_HOTEL",
                                itemDetails
                        );
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

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
}
