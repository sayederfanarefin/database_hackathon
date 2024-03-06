package com.databasecourse.erfan.persistence.dao;

import com.databasecourse.erfan.persistence.model.NewLocationToken;
import com.databasecourse.erfan.persistence.model.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewLocationTokenRepository extends JpaRepository<NewLocationToken, Long> {

    NewLocationToken findByToken(String token);

    NewLocationToken findByUserLocation(UserLocation userLocation);

}
