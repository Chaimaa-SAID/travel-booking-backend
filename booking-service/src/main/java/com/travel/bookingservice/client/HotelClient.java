package com.travel.bookingservice.client;

import com.travel.bookingservice.dto.HotelDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hotel-service", url = "${service.hotel.url}")
public interface HotelClient {

    @GetMapping("/api/hotels/{id}")
    HotelDto getHotelById(@PathVariable("id") Long id);
}
