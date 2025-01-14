package com.ups.uber.strategies;

import com.ups.uber.strategies.impl.DriverMatchingHighestRatingDriver;
import com.ups.uber.strategies.impl.DriverMatchingNearestDriverStrategy;
import com.ups.uber.strategies.impl.RideFareDefaultFareCalculationStrategy;
import com.ups.uber.strategies.impl.RideFareSurgePricingFareCalculationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class StrategyManager {

    @Autowired
    private DriverMatchingHighestRatingDriver highestRatingDriverStrategy;

    @Autowired
    private DriverMatchingNearestDriverStrategy nearestDriverStrategy;

    @Autowired
    private RideFareDefaultFareCalculationStrategy defaultFareCalculationStrategy;

    @Autowired
    private RideFareSurgePricingFareCalculationStrategy surgePricingFareCalculationStrategy;


    public DriverMatchingStrategy driverMatchingStrategy(double riderRaring){
        if(riderRaring >= 4.8){
            return highestRatingDriverStrategy;
        } else {
            return nearestDriverStrategy;
        }

    }



    public RideFareCalculationStrategy rideFareCalculationStrategy(){

        // surge time is from 6PM to 9PM
        LocalTime surgeStartTime = LocalTime.of(18,0);
        LocalTime surgeTimeEnd  = LocalTime.of(21,0);
        LocalTime currentTime = LocalTime.now();

        boolean isSurgeTime = currentTime.isAfter(surgeStartTime) && currentTime.isBefore(surgeTimeEnd);
        if(isSurgeTime){
            return surgePricingFareCalculationStrategy;
        } else{
            return defaultFareCalculationStrategy;
        }

    }
}















