package org.example.volservice.repository;


import org.example.volservice.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByDepartureCity(String departureCity);

    List<Flight> findByDestinationCity(String destinationCity);

    List<Flight> findByPriceLessThan(double price);

    @Modifying
    @Query("UPDATE Flight f SET f.availableSeats = f.availableSeats - 1 WHERE f.id = :id")
    void decrementSeats(@Param("id") Long id);

}
