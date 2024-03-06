package com.databasecourse.erfan.persistence.dao;

import com.databasecourse.erfan.persistence.model.DeviceMetadata;
import com.databasecourse.erfan.persistence.model.Leaderboard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {
    Leaderboard findByHackathon_Id(Long hackathonId);
}
