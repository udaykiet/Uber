package com.ups.uber.strategies;

import com.ups.uber.entities.enums.PaymentMethod;
import com.ups.uber.strategies.impl.CashPaymentStrategy;
import com.ups.uber.strategies.impl.WalletPaymentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentStrategyManager {

    @Autowired
    private CashPaymentStrategy cashPaymentStrategy;

    @Autowired
    private WalletPaymentStrategy walletPaymentStrategy;


    public PaymentStrategy paymentStrategy(PaymentMethod paymentMethod){
        return switch (paymentMethod) {
            case CASH -> cashPaymentStrategy;
            case WALLET -> walletPaymentStrategy;
            default -> throw new RuntimeException("not valid payment method");
        };


    }

}
