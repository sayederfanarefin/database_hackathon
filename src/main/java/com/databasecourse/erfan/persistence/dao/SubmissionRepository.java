package com.databasecourse.erfan.persistence.dao;

import com.databasecourse.erfan.persistence.model.Role;
import com.databasecourse.erfan.persistence.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {


}
