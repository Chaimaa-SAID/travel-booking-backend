package org.example.hotelservice.repository;


import org.example.hotelservice.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
