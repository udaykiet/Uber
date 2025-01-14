package com.ups.uber.services;

import com.ups.uber.entities.Ride;
import com.ups.uber.entities.User;
import com.ups.uber.entities.Wallet;
import com.ups.uber.entities.enums.TransactionMethod;

public interface WalletService {

    Wallet addMoneyToWallet(User user, Double money , String transactionId , Ride ride,
                            TransactionMethod transactionMethod);

    Wallet deductMoneyFromWallet(User user, Double money , String transactionId , Ride ride,
                                 TransactionMethod transactionMethod);

    void withdrawAllMyMoneyFromWallet();

    Wallet findWalletById(Long walletId);

    Wallet createNewWallet(User user);

    Wallet findByUser(User user );

}
