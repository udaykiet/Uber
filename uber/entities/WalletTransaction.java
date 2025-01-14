package com.ups.uber.entities;

import com.ups.uber.entities.enums.TransactionMethod;
import com.ups.uber.entities.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(
        indexes = {
                @Index(name = "idx_wallet_transaction_wallet" , columnList = "wallet_id"),
                @Index(name = "idx_wallet_transaction_ride" , columnList = "ride_id")
        }
)
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private TransactionMethod transactionMethod;

    @ManyToOne
    private Ride ride;


    @ManyToOne
    private Wallet wallet;


    private String transactionId;

    @CreationTimestamp
    private LocalDateTime timeStamp;
}














