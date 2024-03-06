package com.databasecourse.erfan.persistence.dao;

import com.databasecourse.erfan.persistence.model.Testcase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCaseRepository extends JpaRepository<Testcase, Long> {



}
