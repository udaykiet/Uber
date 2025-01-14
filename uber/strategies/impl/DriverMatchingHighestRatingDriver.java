package com.ups.uber.strategies.impl;

import com.ups.uber.entities.Driver;
import com.ups.uber.entities.RideRequest;
import com.ups.uber.repositories.DriverRepository;
import com.ups.uber.strategies.DriverMatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverMatchingHighestRatingDriver implements DriverMatchingStrategy {

    @Autowired
    private DriverRepository driverRepository;


    @Override
    public List<Driver> findMatchingDrivers(RideRequest rideRequest) {
        return driverRepository.findTenNearbyTopRatedDrivers(rideRequest.getPickUpLocation());
    }
}
