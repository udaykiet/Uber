package com.ups.uber.services;

import com.ups.uber.dtos.DriverDto;
import com.ups.uber.dtos.RideDto;
import com.ups.uber.dtos.RideRequestDto;
import com.ups.uber.dtos.RiderDto;
import com.ups.uber.entities.Rider;
import com.ups.uber.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RiderService {

    RideRequestDto requestRide(RideRequestDto rideRequestDto);

    RideDto cancelRide(Long rideId);

    DriverDto rateDriver(Long rideId , Integer rating);

    RiderDto getMyProfile();

    Page<RideDto> getAllMyRides(PageRequest pageRequest);

    Rider createNewRider(User user);

    Rider getCurrentRider();
}
