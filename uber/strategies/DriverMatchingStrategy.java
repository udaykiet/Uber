package com.ups.uber.strategies;

import com.ups.uber.entities.Driver;
import com.ups.uber.entities.RideRequest;

import java.util.List;

public interface DriverMatchingStrategy {

    List<Driver> findMatchingDrivers(RideRequest rideRequest);

}
