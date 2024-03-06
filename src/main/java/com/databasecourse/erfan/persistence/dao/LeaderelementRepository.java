package com.databasecourse.erfan.persistence.dao;

import com.databasecourse.erfan.persistence.model.Leaderboard;
import com.databasecourse.erfan.persistence.model.Leaderelement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaderelementRepository extends JpaRepository<Leaderelement, Long> {
    List<Leaderelement> findByUserIdOriginal(Long userIdOriginal);
    Leaderelement findByUserIdOriginalAndLeaderboard_Hackathon_Id(Long userIdOriginal, Long id);
}
