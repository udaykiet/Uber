package com.ups.uber.services.impl;

import com.ups.uber.dtos.DriverDto;
import com.ups.uber.dtos.RideDto;
import com.ups.uber.dtos.RiderDto;
import com.ups.uber.entities.Driver;
import com.ups.uber.entities.Ride;
import com.ups.uber.entities.RideRequest;
import com.ups.uber.entities.User;
import com.ups.uber.entities.enums.RideRequestStatus;
import com.ups.uber.entities.enums.RideStatus;
import com.ups.uber.exceptions.ResourceNotFoundException;
import com.ups.uber.repositories.DriverRepository;
import com.ups.uber.services.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private RideRequestService rideRequestService;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private RideService rideService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RatingService ratingService;

    @Override
    public RideDto acceptRide(Long rideRequestId) {

        RideRequest rideRequest = rideRequestService.findRideRequestById(rideRequestId);
        if(!rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING)){
            throw new RuntimeException("RideRequest cannot be accepted as status is "+rideRequest.getRideRequestStatus());
        }

        Driver currentDriver = getCurrentDriver();
        if(!currentDriver.getAvailable()){
            throw new RuntimeException("driver is not available");
        }

        Driver savedDriver =updateDriverAvailability(currentDriver , false);
        Ride ride = rideService.createNewRide(rideRequest,savedDriver);
        return modelMapper.map(ride , RideDto.class);
    }

    @Override
    public RideDto cancelRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();
        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot start a ride as he has not accept it earlier");
        }

        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("Ride cannot be cancelled , invalid ride status: " + ride.getRideStatus());
        }
        rideService.updateRideStatus(ride , RideStatus.CANCELLED);

        updateDriverAvailability(driver, true);
        return modelMapper.map(ride, RideDto.class);
    }

    @Override
    public RideDto startRide(Long rideId , String otp) {

        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();
        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot start a ride as he has not accept it earlier");
        }

        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("ride status is not confirmed hence cannot start a ride , status: "+ ride.getRideStatus());
        }

        if(!ride.getOtp().equals(otp)){
            throw new RuntimeException("provided otp is wrong  , otp : "+ otp);
        }


        ride.setStartedAt(LocalDateTime.now());
        Ride savedRide =  rideService.updateRideStatus(ride , RideStatus.ONGOING);


        paymentService.createNewPayment(savedRide); // this will create a new payment entry fot the ride .. and save in the db
        ratingService.createNewRating(ride); // this will create a new rating entyr fot the rde ..  and save in the db

        return modelMapper.map(savedRide , RideDto.class);


    }

    @Override
    public RideDto endRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();
        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot start a ride as he has not accept it earlier");
        }

        if(!ride.getRideStatus().equals(RideStatus.ONGOING)){
            throw new RuntimeException("ride status is not ongoing hence cannot ended a ride , status: "+ ride.getRideStatus());
        }

        ride.setEndedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride,RideStatus.ENDED);
        updateDriverAvailability(driver,true);
        paymentService.processPayment(ride);

        return modelMapper.map(savedRide, RideDto.class);

    }

    @Override
    public RiderDto rateRider(Long rideId, Integer rating) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver  = getCurrentDriver();

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("driver with id: " + driver.getId() + " does not owns this ride.");
        }

        if(!ride.getRideStatus().equals(RideStatus.ENDED)){
            throw new RuntimeException("cant rate the rider with id: " + ride.getRider().getId() + " as ride is not end yet.");
        }

        return ratingService.rateRider(ride , rating);
    }

    @Override
    public DriverDto getMyProfile() {
        Driver currentDriver = getCurrentDriver();
        return modelMapper.map(currentDriver , DriverDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        Driver currentDriver = getCurrentDriver();
       return rideService.getAllRidesOfDriver(currentDriver, pageRequest).map(
               ride -> modelMapper.map(ride , RideDto.class)
       );
    }

    @Override
    public Driver getCurrentDriver() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return driverRepository.findByUser(user)
                .orElseThrow(()-> new ResourceNotFoundException("driver is not found"));
    }

    @Override
    public Driver updateDriverAvailability(Driver driver, boolean available) {
        driver.setAvailable(available);
        return driverRepository.save(driver);
    }

    @Override
    public Driver createNewDriver(Driver driver) {
        return driverRepository.save(driver);
    }
}
