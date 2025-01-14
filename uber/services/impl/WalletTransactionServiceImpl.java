package com.ups.uber.services.impl;

import com.ups.uber.entities.WalletTransaction;
import com.ups.uber.repositories.WalletTransactionRepository;
import com.ups.uber.services.WalletTransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletTransactionServiceImpl implements WalletTransactionService {

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void createNewWalletTransaction(WalletTransaction walletTransaction) {
        walletTransactionRepository.save(walletTransaction);
    }
}
