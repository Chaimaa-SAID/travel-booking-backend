package org.example.hotelservice.controller;

import org.example.hotelservice.model.Hotel;
import org.example.hotelservice.repository.HotelRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelRepository hotelRepository;

    public HotelController(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    // USER et ADMIN peuvent consulter
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public Hotel getHotel(@PathVariable Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
    }

    // ADMIN seulement peut créer
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Hotel createHotel(@RequestBody Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    // ADMIN seulement peut modifier
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Hotel updateHotel(@PathVariable Long id, @RequestBody Hotel hotel) {
        Hotel h = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        h.setName(hotel.getName());
        h.setCity(hotel.getCity());
        h.setAddress(hotel.getAddress());
        h.setPricePerNight(hotel.getPricePerNight());
        h.setAvailableRooms(hotel.getAvailableRooms());
        return hotelRepository.save(h);
    }

    // ADMIN seulement peut supprimer
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteHotel(@PathVariable Long id) {
        hotelRepository.deleteById(id);
    }
}
