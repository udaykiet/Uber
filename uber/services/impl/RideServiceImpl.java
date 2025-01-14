package com.ups.uber.services.impl;

import com.ups.uber.dtos.RideRequestDto;
import com.ups.uber.entities.Driver;
import com.ups.uber.entities.Ride;
import com.ups.uber.entities.RideRequest;
import com.ups.uber.entities.Rider;
import com.ups.uber.entities.enums.RideRequestStatus;
import com.ups.uber.entities.enums.RideStatus;
import com.ups.uber.repositories.RideRepository;
import com.ups.uber.services.RideRequestService;
import com.ups.uber.services.RideService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RideServiceImpl implements RideService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private RideRequestService rideRequestService;



    @Override
    public Ride getRideById(Long rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(()-> new RuntimeException("ride not found with rideId: "+rideId));
    }


    @Override
    public void matchWithDrivers(RideRequestDto rideRequestDto) {

    }

    @Override
    public Ride createNewRide(RideRequest rideRequest, Driver driver) {
        rideRequest.setRideRequestStatus(RideRequestStatus.CONFIRMED);
        Ride ride = modelMapper.map(rideRequest , Ride.class);
        ride.setRideStatus(RideStatus.CONFIRMED);
        ride.setDriver(driver);
        ride.setOtp(generateRandomOTP());
        ride.setId(null);

        rideRequestService.update(rideRequest);
        return rideRepository.save(ride);


    }

    @Override
    public Ride updateRideStatus(Ride ride, RideStatus rideStatus) {
        ride.setRideStatus(rideStatus);
        return rideRepository.save(ride);


    }

    @Override
    public Page<Ride> getAllRidesOfRider(Rider rider , PageRequest pageRequest) {
        return rideRepository.findByRider(rider, pageRequest);
    }

    @Override
    public Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest) {
        return rideRepository.findByDriver(driver ,pageRequest);
    }


    private String generateRandomOTP(){
        Random random = new Random();
        int otpInt = random.nextInt(10000);
        return String.format("%04d" ,otpInt);
    }
}
