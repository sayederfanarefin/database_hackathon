package com.databasecourse.erfan.web.util;

import com.databasecourse.erfan.persistence.model.Hackathon;
import com.databasecourse.erfan.persistence.model.User;
import com.databasecourse.erfan.registration.OnLeaderboardCheckEvent;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CheckOnGoingHackathon {

    public static boolean isHackathonGoingOn(Hackathon hackathon){
        LocalDateTime currentDateTime = LocalDateTime.now();
        return currentDateTime.isAfter( hackathon.getStartDateTime()) && currentDateTime.isBefore( hackathon.getEndDateTime());
    }
}
