package com.travel.bookingservice.client;

import com.travel.bookingservice.dto.FlightDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "vol-service", url = "${service.vol.url}")
public interface FlightClient {

    @GetMapping("/api/flights/{id}")
    FlightDto getFlightById(@PathVariable("id") Long id);
}
