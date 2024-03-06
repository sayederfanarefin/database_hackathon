package com.databasecourse.erfan.persistence.dao;

import com.databasecourse.erfan.persistence.model.User;
import com.databasecourse.erfan.persistence.model.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {
    UserLocation findByCountryAndUser(String country, User user);

}
