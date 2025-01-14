package com.ups.uber.services.impl;

import com.ups.uber.entities.Ride;
import com.ups.uber.entities.User;
import com.ups.uber.entities.Wallet;
import com.ups.uber.entities.WalletTransaction;
import com.ups.uber.entities.enums.TransactionMethod;
import com.ups.uber.entities.enums.TransactionType;
import com.ups.uber.repositories.WalletRepository;
import com.ups.uber.services.WalletService;
import com.ups.uber.services.WalletTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletTransactionService walletTransactionService;


    @Override
    @Transactional
    public Wallet addMoneyToWallet(User user, Double money , String transactionId , Ride ride,
                                   TransactionMethod transactionMethod) {
        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance() + money);

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionId(transactionId)
                .wallet(wallet)
                .ride(ride)
                .transactionMethod(transactionMethod)
                .transactionType(TransactionType.CREDIT)
                .amount(money)
                .build();

        walletTransactionService.createNewWalletTransaction(walletTransaction);
//        wallet.getWalletTransactions().add(walletTransaction);
        return walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public Wallet deductMoneyFromWallet(User user, Double money , String transactionId , Ride ride,
                                        TransactionMethod transactionMethod) {
        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance() - money);

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionId(transactionId)
                .wallet(wallet)
                .ride(ride)
                .transactionMethod(transactionMethod)
                .transactionType(TransactionType.DEBIT)
                .amount(money)
                .build();

        walletTransactionService.createNewWalletTransaction(walletTransaction);
//        wallet.getWalletTransactions().add(walletTransaction);
        return walletRepository.save(wallet);
    }

    @Override
    public void withdrawAllMyMoneyFromWallet() {

    }

    @Override
    public Wallet findWalletById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(()->new RuntimeException("Wallet with id:" + walletId + " is not present"));
    }

    @Override
    public Wallet createNewWallet(User user) {
       Wallet wallet = new Wallet();
       wallet.setUser(user);
       return walletRepository.save(wallet);

    }

    @Override
    public Wallet findByUser(User user) {
        return walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("wallet not found , for user: " + user));
    }
}
