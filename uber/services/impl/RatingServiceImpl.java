package com.ups.uber.services.impl;

import com.ups.uber.dtos.DriverDto;
import com.ups.uber.dtos.RiderDto;
import com.ups.uber.entities.Driver;
import com.ups.uber.entities.Rating;
import com.ups.uber.entities.Ride;
import com.ups.uber.entities.Rider;
import com.ups.uber.exceptions.ResourceNotFoundException;
import com.ups.uber.repositories.DriverRepository;
import com.ups.uber.repositories.RatingRepository;
import com.ups.uber.repositories.RiderRepository;
import com.ups.uber.services.RatingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public DriverDto rateDriver(Ride ride, Integer rating) {
        Driver driver = ride.getDriver();
        Rating ratingObj = ratingRepository.findByride(ride)
                .orElseThrow(()->new ResourceNotFoundException("Rating not found for the ride with id: " + ride.getId()));

        if(ratingObj.getDriverRating() != null)
            throw new RuntimeException("Driver rating for this ride is already been set");

        ratingObj.setDriverRating(rating);
        ratingRepository.save(ratingObj);

        Double newRating = ratingRepository.findByDriver(driver)
                .stream()
                .mapToDouble(rating1 -> rating1.getDriverRating())
                .average().orElse(0.0);

        driver.setRating(newRating);
        return modelMapper.map(driverRepository.save(driver), DriverDto.class);

    }

    @Override
    public RiderDto rateRider(Ride ride, Integer rating) {
        Rider rider = ride.getRider();
        Rating ratingObj = ratingRepository.findByride(ride)
                .orElseThrow(()->new ResourceNotFoundException("Rating not found for the ride with id: " + ride.getId()));
        if(ratingObj.getRiderRating() != null)
            throw new RuntimeException("Rider rating for this ride is already been set");

        ratingObj.setRiderRating(rating);
        ratingRepository.save(ratingObj);

        Double newRating = ratingRepository.findByRider(rider)
                .stream()
                .mapToDouble(rating1 -> rating1.getRiderRating())
                .average().orElse(0.0);

        rider.setRating(newRating);
        return modelMapper.map(riderRepository.save(rider), RiderDto.class);
    }

    @Override
    public void createNewRating(Ride ride) {
        Rating rating  = Rating.builder()
                .driver(ride.getDriver())
                .rider(ride.getRider())
                .ride(ride)
                .build();

        ratingRepository.save(rating);
    }
}
