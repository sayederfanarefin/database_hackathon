package com.databasecourse.erfan.service;


import com.databasecourse.erfan.persistence.model.*;
import com.databasecourse.erfan.web.dto.UserDashScoreBreakDownDto;

import java.util.List;

public interface ILeaderboardService {
    List<Leaderelement> getHackathonLeaderboard(Long hackathonId);
    void rearrangeLeederelementsByPoints(Long leederBoardId);
    List<Leaderelement> getCurrentLeaderboard();

    Hackathon getCurrentHackathon();

    void updateLeaderElement(User user);

    Integer getUserPosition(User user);

    Double completedUserTasksPercentage(User user, Hackathon  hackathon);

    Integer completedUserTasks(User user, Hackathon  hackathon);

    List<UserDashScoreBreakDownDto> getUserDashScoreBreakDown(User user, Hackathon hackathon);

    Double averageAttemptsAllUser( Long hackathonId, Long taskId);


    Leaderboard findByHackathon(Long hackathonId);
    void updateLeaderElementByUserTaskHackathon(Leaderelement leaderelement);

}
