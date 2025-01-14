package com.ups.uber.services.impl;

import com.ups.uber.entities.Payment;
import com.ups.uber.entities.Ride;
import com.ups.uber.entities.enums.PaymentStatus;
import com.ups.uber.exceptions.ResourceNotFoundException;
import com.ups.uber.repositories.PaymentRepository;
import com.ups.uber.services.PaymentService;
import com.ups.uber.strategies.PaymentStrategyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentStrategyManager paymentStrategyManager;

    @Override
    public void processPayment(Ride ride) {
        Payment payment = paymentRepository.findByRide(ride)
                        .orElseThrow(()-> new ResourceNotFoundException
                                ("Payment not found for the ride with id: " + ride.getId()));

        paymentStrategyManager.paymentStrategy(payment.getPaymentMethod()).processPayment(payment);
    }

    @Override
    public Payment createNewPayment(Ride ride) {
        Payment payment = Payment.builder()
                .paymentMethod(ride.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .amount(ride.getFare())
                .ride(ride)
                .build();

        return paymentRepository.save(payment);

    }

    @Override
    public void updatePaymentStatus(Payment payment, PaymentStatus paymentStatus) {
        payment.setPaymentStatus(paymentStatus);
        paymentRepository.save(payment);
    }
}
