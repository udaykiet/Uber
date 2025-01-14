package com.ups.uber.strategies.impl;

import com.ups.uber.entities.RideRequest;
import com.ups.uber.services.DistanceService;
import com.ups.uber.strategies.RideFareCalculationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RideFareDefaultFareCalculationStrategy implements RideFareCalculationStrategy {

    @Autowired
    private DistanceService distanceService;

    @Override
    public double calculateFare(RideRequest rideRequest) {
        double distance = distanceService.calculateDistance(rideRequest.getPickUpLocation(),
                rideRequest.getDropOffLocation());
        return distance*RIDE_FARE_MULTIPLIER;


    }
}
