package com.ups.uber.repositories;

import com.ups.uber.entities.Driver;
import com.ups.uber.entities.Rating;
import com.ups.uber.entities.Ride;
import com.ups.uber.entities.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository  extends JpaRepository<Rating , Long> {

    List<Rating> findByRider(Rider rider);
    List<Rating> findByDriver(Driver driver);

    Optional<Rating> findByride(Ride ride);
}
