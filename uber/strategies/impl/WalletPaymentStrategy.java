package com.ups.uber.strategies.impl;

import com.ups.uber.entities.Driver;
import com.ups.uber.entities.Payment;
import com.ups.uber.entities.Rider;
import com.ups.uber.entities.enums.PaymentStatus;
import com.ups.uber.entities.enums.TransactionMethod;
import com.ups.uber.repositories.PaymentRepository;
import com.ups.uber.services.WalletService;
import com.ups.uber.strategies.PaymentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class WalletPaymentStrategy implements PaymentStrategy {

    @Autowired
    private WalletService walletService;


    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    @Transactional
    public void processPayment(Payment payment) {

        Driver driver = payment.getRide().getDriver();
        Rider rider = payment.getRide().getRider();

        walletService.deductMoneyFromWallet(rider.getUser(),
                payment.getAmount(),
                null,
                payment.getRide(),
                TransactionMethod.RIDE);

        double driverCut = payment.getAmount() * (1 - PLATFORM_COMMISSION);

        walletService.addMoneyToWallet(driver.getUser(),
                driverCut,
                null,
                payment.getRide(),
                TransactionMethod.RIDE);

        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);

    }
}
