package com.ups.uber.dtos;

import com.ups.uber.entities.enums.TransactionMethod;
import com.ups.uber.entities.enums.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WalletTransactionDto {
    private Long id;
    private Double amount;
    private TransactionType transactionType;
    private TransactionMethod transactionMethod;
    private RideDto ride;
    private WalletDto wallet;
    private String transactionId;
    private LocalDateTime timeStamp;
}
