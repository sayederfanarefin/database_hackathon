package com.databasecourse.erfan.service;


import com.databasecourse.erfan.persistence.model.Hackathon;

import java.io.IOException;

public interface IGradingService {
    Integer getNumberOfAttempts(Long userId, Long hackathonId, Long taskId);
    void updateNumberOfAttempts(Long userId, Long hackathonId, Long taskId, Integer numberOfAttempts);

    String checkSubmission(Long userId, Long taskId, String taskInputUser, Long hackathonId) throws IOException;

    boolean viewDataBase(String databaseName);

    Hackathon getCurrentHackathonForUser(Long userId);
    String startTime(Long hackathonId, Long taskId, Long userId);
    void updateLeederBoard();
}
