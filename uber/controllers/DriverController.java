package com.ups.uber.controllers;

import com.ups.uber.dtos.*;
import com.ups.uber.services.DriverService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver")
@Secured("ROLE_DRIVER")
@Tag(name = "Driver APIs" , description = "")
public class DriverController {

    @Autowired
    private DriverService driverService;


    @PostMapping("/acceptRide/{rideRequestId}")
    public ResponseEntity<RideDto> acceptRide(@PathVariable Long rideRequestId){
        return ResponseEntity.ok(driverService.acceptRide(rideRequestId));
    }

    @PostMapping("/startRide/{rideId}")
    public ResponseEntity<RideDto> startRide(@PathVariable Long rideId,
                                              @RequestBody OtpDto otpDto){
        return ResponseEntity.ok(driverService.startRide(rideId , otpDto.getOtp()));
    }

    @PostMapping("/endRide/{rideId}")
    public ResponseEntity<RideDto> startRide(@PathVariable Long rideId){
        return ResponseEntity.ok(driverService.endRide(rideId));
    }


    @PostMapping("/cancelRide/{rideId}")
    public ResponseEntity<RideDto> cancelRide(@PathVariable Long rideId){
        return ResponseEntity.ok(driverService.cancelRide(rideId));
    }

    @PostMapping("/rateRider")
    public ResponseEntity<RiderDto> rateRider(@RequestBody RatingDto ratingDto){
        return ResponseEntity.ok(driverService.rateRider(ratingDto.getRideId(), ratingDto.getRating()));
    }

    @GetMapping("/getMyProfile")
    public ResponseEntity<DriverDto> getMyProfile(){
        return ResponseEntity.ok(driverService.getMyProfile());
    }

    @GetMapping("/getAllMyRides")
    public ResponseEntity<Page<RideDto>> getAllMyRides(@RequestParam(defaultValue = "0" , required = false)  Integer pageOffset,
                                                       @RequestParam(defaultValue = "10" , required = false) Integer pageSize){
        PageRequest pageRequest = PageRequest.of(pageOffset , pageSize,
                Sort.by(Sort.Direction.DESC , "createdTime"));
        return ResponseEntity.ok(driverService.getAllMyRides(pageRequest));
    }





}
