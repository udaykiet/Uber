package com.ups.uber.services.impl;

import com.ups.uber.dtos.DriverDto;
import com.ups.uber.dtos.RideDto;
import com.ups.uber.dtos.RideRequestDto;
import com.ups.uber.dtos.RiderDto;
import com.ups.uber.entities.*;
import com.ups.uber.entities.enums.RideRequestStatus;
import com.ups.uber.entities.enums.RideStatus;
import com.ups.uber.exceptions.ResourceNotFoundException;
import com.ups.uber.repositories.RideRequestRepository;
import com.ups.uber.repositories.RiderRepository;
import com.ups.uber.services.DriverService;
import com.ups.uber.services.RatingService;
import com.ups.uber.services.RideService;
import com.ups.uber.services.RiderService;
import com.ups.uber.strategies.StrategyManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RiderServiceImpl implements RiderService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private StrategyManager strategyManager;

    @Autowired
    private RideRequestRepository rideRequestRepository;

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private RideService rideService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private RatingService ratingService;


    @Override
    @Transactional
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {
        Rider rider = getCurrentRider();
        RideRequest rideRequest = modelMapper.map(rideRequestDto , RideRequest.class);
        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);
        rideRequest.setRider(rider);
        Double fare = strategyManager.rideFareCalculationStrategy().calculateFare(rideRequest);
        rideRequest.setFare(fare);

         RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);

        List<Driver> drivers = strategyManager
                .driverMatchingStrategy(rider.getRating()).findMatchingDrivers(rideRequest);

//        todo : send notification to all these drivers about the ride request

        return modelMapper.map(savedRideRequest , RideRequestDto.class);
    }

    @Override
    public RideDto cancelRide(Long rideId) {

        Rider rider = getCurrentRider();
        Ride ride = rideService.getRideById(rideId);
        if(!rider.equals(ride.getRider())){
            throw new RuntimeException("Rider doesn't own this ride with rideId: " + rideId);
        }
        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("Ride cannot be cancelled , invalid ride status: " + ride.getRideStatus());
        }
        Ride savedRide = rideService.updateRideStatus(ride , RideStatus.CANCELLED);
        driverService.updateDriverAvailability(ride.getDriver(),true);
        return modelMapper.map(savedRide, RideDto.class);
    }

    @Override
    public DriverDto rateDriver(Long rideId, Integer rating) {
        Ride ride = rideService.getRideById(rideId);
        Rider currentRider = getCurrentRider();

        if(!currentRider.equals(ride.getRider())){
            throw new RuntimeException("Rider with the id: " + currentRider.getId() + " does not owns the ride");
        }

        if(!ride.getRideStatus().equals(RideStatus.ENDED)){
            throw new RuntimeException("cant rate the driver with id: " + ride.getDriver().getId() + " as ride is not end yet.");
        }
        return ratingService.rateDriver(ride , rating);
    }

    @Override
    public RiderDto getMyProfile() {
      Rider currentRider = getCurrentRider();
      return modelMapper.map(currentRider , RiderDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
       Rider currentRider = getCurrentRider();
        return rideService.getAllRidesOfRider(currentRider, pageRequest).map(
                ride -> modelMapper.map(ride , RideDto.class)
        );
    }

    @Override
    public Rider createNewRider(User user) {
//        Rider rider = Rider.builder()
//                .user(user)
//                .rating(0.0)
//                .build();

        Rider rider = new Rider();
        rider.setUser(user);
        rider.setRating(0.0);
        return riderRepository.save(rider);
    }

    @Override
    public Rider getCurrentRider() {
//        TODO implement the spring security to get the current rider
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return riderRepository.findByUser(user)
                .orElseThrow(()-> new ResourceNotFoundException("rider not found"));

    }
}
