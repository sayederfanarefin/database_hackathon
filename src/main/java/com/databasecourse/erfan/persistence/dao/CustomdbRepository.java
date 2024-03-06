package com.databasecourse.erfan.persistence.dao;

import com.databasecourse.erfan.persistence.model.Customdb;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomdbRepository extends JpaRepository<Customdb, Long> {

    Optional<Customdb> findById(Long id);


}
