package com.databasecourse.erfan.persistence.dao;

import com.databasecourse.erfan.persistence.model.Hackathon;
import com.databasecourse.erfan.persistence.model.Task;
import com.databasecourse.erfan.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface HackathonRepository extends JpaRepository<Hackathon, Long> {



    @Query("SELECT g FROM Hackathon g ORDER BY g.startDateTime ASC")
    List<Hackathon> findAllSortedByStartDateTime();

    List<Hackathon> findAllByUsers_IdAndStartDateTimeIsBeforeAndEndDateTimeIsAfter(Long userId, LocalDateTime before, LocalDateTime after);
    List<Hackathon> findAllByUsers_Id(Long userId);

    @Query("SELECT g FROM Hackathon g WHERE g.endDateTime < :currentTime AND :user MEMBER OF g.users ORDER BY g.endDateTime ASC")
    List<Hackathon> findAllPastHacks(@Param("currentTime") LocalDateTime currentTime, @Param("user") User user);


}
