package com.ups.uber.strategies.impl;

import com.ups.uber.entities.Driver;
import com.ups.uber.entities.Payment;
import com.ups.uber.entities.enums.PaymentStatus;
import com.ups.uber.entities.enums.TransactionMethod;
import com.ups.uber.repositories.PaymentRepository;
import com.ups.uber.services.WalletService;
import com.ups.uber.strategies.PaymentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CashPaymentStrategy implements PaymentStrategy {

    @Autowired
    private WalletService walletService;

    @Autowired
    private PaymentRepository paymentRepository;


    @Override
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();
        double platformCommission = payment.getAmount() * PLATFORM_COMMISSION;
        walletService.deductMoneyFromWallet( driver.getUser(),
                platformCommission,null ,
                payment.getRide(),TransactionMethod.RIDE);



        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);



    }
}














