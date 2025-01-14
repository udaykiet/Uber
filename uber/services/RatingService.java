package com.ups.uber.services;

import com.ups.uber.dtos.DriverDto;
import com.ups.uber.dtos.RiderDto;
import com.ups.uber.entities.Driver;
import com.ups.uber.entities.Ride;
import com.ups.uber.entities.Rider;

public interface RatingService {

    DriverDto rateDriver(Ride ride , Integer rating);
    RiderDto rateRider(Ride ride , Integer rating);

    void createNewRating(Ride ride);
}
