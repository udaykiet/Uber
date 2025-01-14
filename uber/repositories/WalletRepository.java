package com.ups.uber.repositories;

import com.ups.uber.entities.User;
import com.ups.uber.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet , Long> {
    Optional<Wallet> findByUser(User user);
}
