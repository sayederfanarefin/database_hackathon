package com.databasecourse.erfan.persistence.dao;

import com.databasecourse.erfan.persistence.model.Task;
import com.databasecourse.erfan.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findByTitle(String title);

    List<Task> findAllByHackathons_Id(Long hackathonId);
    List<Task> findAllByIdAndHackathons_Id(Long taskId, Long hackathonId);
//    List<Task> findAllByUsers_idAndHackathons_id(Long userId, Long hackathonId);


}
