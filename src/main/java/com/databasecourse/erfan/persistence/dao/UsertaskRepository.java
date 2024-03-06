package com.databasecourse.erfan.persistence.dao;

import com.databasecourse.erfan.persistence.model.Usertask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
//@Transactional
public interface UsertaskRepository extends JpaRepository<Usertask, Long> {


    Usertask findByUser_IdAndTask_IdAndHackathonId(Long userId, Long taskId, Long hackathonId);
    List<Usertask> findByTask_Id(Long id);


    List<Usertask> findByUser_IdAndHackathonId(Long userId, Long hackathonId);
    List<Usertask> findByTask_IdAndHackathonId(Long taskId, Long hackathonId);


}
