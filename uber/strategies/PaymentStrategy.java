package com.ups.uber.strategies;

import com.ups.uber.entities.Payment;

public interface PaymentStrategy {

    Double PLATFORM_COMMISSION = 0.3;

    void processPayment(Payment payment);
}
