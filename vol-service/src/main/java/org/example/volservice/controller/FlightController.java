package org.example.volservice.controller;

import org.example.volservice.model.Flight;
import org.example.volservice.repository.FlightRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightRepository flightRepository;

    public FlightController(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    // USER et ADMIN peuvent consulter
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public Flight getFlight(@PathVariable Long id) {
        return flightRepository.findById(id).orElseThrow();
    }

    // ADMIN seulement peut créer
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Flight save(@RequestBody Flight flight) {
        return flightRepository.save(flight);
    }

    // ADMIN seulement peut modifier
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Flight update(@PathVariable Long id, @RequestBody Flight flight) {
        Flight f = flightRepository.findById(id).orElseThrow();
        f.setPrice(flight.getPrice());
        f.setAvailableSeats(flight.getAvailableSeats());
        return flightRepository.save(f);
    }

    // ADMIN seulement peut supprimer
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        flightRepository.deleteById(id);
    }
}
