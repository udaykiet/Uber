package com.ups.uber.services;

import com.ups.uber.entities.Payment;
import com.ups.uber.entities.Ride;
import com.ups.uber.entities.enums.PaymentStatus;

public interface PaymentService {

    void processPayment(Ride ride);

    Payment createNewPayment(Ride ride);

    void updatePaymentStatus(Payment payment , PaymentStatus paymentStatus);
}
