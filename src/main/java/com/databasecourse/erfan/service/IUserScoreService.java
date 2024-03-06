package com.databasecourse.erfan.service;


import com.databasecourse.erfan.persistence.model.Hackathon;
import com.databasecourse.erfan.persistence.model.Leaderboard;
import com.databasecourse.erfan.persistence.model.Leaderelement;
import com.databasecourse.erfan.persistence.model.User;
import com.databasecourse.erfan.web.dto.UserDashScoreBreakDownDto;

import java.util.List;

public interface IUserScoreService {

    List<UserDashScoreBreakDownDto> getUserDashScoreBreakDown(User user, Hackathon hackathon);

}
