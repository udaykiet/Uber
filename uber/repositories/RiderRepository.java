package com.ups.uber.repositories;

import com.ups.uber.entities.Rider;
import com.ups.uber.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiderRepository extends JpaRepository<Rider , Long> {
    Optional<Rider> findByUser(User user);
}

