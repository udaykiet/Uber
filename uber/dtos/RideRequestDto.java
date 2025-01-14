package com.ups.uber.dtos;

import com.ups.uber.entities.enums.PaymentMethod;
import com.ups.uber.entities.enums.RideRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RideRequestDto {
    private PointDto pickUpLocation;
    private PointDto dropOffLocation;
    private LocalDateTime requestedTime;
    private RiderDto rider;
    private PaymentMethod paymentMethod;
    private RideRequestStatus rideRequestStatus;
    private Double fare;
}
