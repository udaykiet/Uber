package com.ups.uber.services.impl;

import com.ups.uber.entities.RideRequest;
import com.ups.uber.exceptions.ResourceNotFoundException;
import com.ups.uber.repositories.RideRequestRepository;
import com.ups.uber.services.RideRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RideRequestServiceImpl implements RideRequestService {

    @Autowired
    private RideRequestRepository rideRequestRepository;

    @Override
    public RideRequest findRideRequestById(Long rideRequestId) {
        return rideRequestRepository.findById(rideRequestId)
                .orElseThrow(()-> new ResourceNotFoundException("rideRequest with id " + rideRequestId + " is not found "));

    }

    @Override
    public void update(RideRequest rideRequest) {

       rideRequestRepository.findById(rideRequest.getId())
                .orElseThrow(()-> new ResourceNotFoundException("rideRequest with id "+rideRequest.getId() +" is not found"));
        rideRequestRepository.save(rideRequest);
    }
}
