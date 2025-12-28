package com.travel.bookingservice.controller;

import com.travel.bookingservice.dto.BookingRequest;
import com.travel.bookingservice.dto.BookingResponse;
import com.travel.bookingservice.model.Booking;
import com.travel.bookingservice.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // USER peut créer une réservation
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public Mono<ResponseEntity<BookingResponse>> createBooking(@RequestBody BookingRequest req) {
        return bookingService.createBooking(req)
                .map(res -> ResponseEntity.ok(res));
    }

    // USER peut consulter sa réservation
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable Long id) {
        Booking b = bookingService.getBooking(id);
        if (b == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(b);
    }

    // USER peut mettre à jour le statut (ex: annuler)
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> updateBooking(@PathVariable Long id, @RequestParam String status) {
        Booking booking = bookingService.getBooking(id);
        if (booking == null) return ResponseEntity.notFound().build();
        booking.setStatus(status);
        bookingService.saveBooking(booking);
        return ResponseEntity.ok(
                new BookingResponse(
                        booking.getId(),           // bookingId
                        booking.getStatus(),       // status
                        booking.getTotalPrice(),   // totalPrice
                        "FLIGHT_HOTEL",            // type (ou "FLIGHT"/"HOTEL" selon ton cas)
                        null                       // itemDetails (tu peux mettre null si tu ne veux pas les détails ici)
                )
        );
    }

    // ADMIN peut supprimer une réservation
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        Booking booking = bookingService.getBooking(id);
        if (booking == null) return ResponseEntity.notFound().build();
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable Long userId) {
        List<Booking> bookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }


}
