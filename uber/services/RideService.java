package com.ups.uber.services;

import com.ups.uber.dtos.RideRequestDto;
import com.ups.uber.entities.Driver;
import com.ups.uber.entities.Ride;
import com.ups.uber.entities.RideRequest;
import com.ups.uber.entities.Rider;
import com.ups.uber.entities.enums.RideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RideService {

    Ride getRideById(Long rideId);

    void matchWithDrivers(RideRequestDto rideRequestDto);

    Ride createNewRide(RideRequest rideRequest , Driver driver);

    Ride updateRideStatus(Ride ride , RideStatus rideStatus);

    Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest);

    Page<Ride> getAllRidesOfDriver(Driver driver , PageRequest pageRequest);
}
