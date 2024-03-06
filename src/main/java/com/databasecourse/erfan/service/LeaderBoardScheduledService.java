//package com.databasecourse.erfan.service;
//
//import com.databasecourse.erfan.Constants;
//import com.databasecourse.erfan.persistence.dao.HackathonRepository;
//import com.databasecourse.erfan.persistence.dao.TaskRepository;
//import com.databasecourse.erfan.persistence.model.Hackathon;
//import com.databasecourse.erfan.persistence.model.User;
//import com.databasecourse.erfan.registration.OnLeaderboardCheckEvent;
//import com.databasecourse.erfan.web.util.CheckOnGoingHackathon;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static java.util.stream.Collectors.toList;
//
//
//
//@Service
//@Transactional
//public class LeaderBoardScheduledService  {
//
//    @Autowired
//    private ApplicationEventPublisher eventPublisher;
//
//    @Autowired
//    private HackathonRepository hackathonRepository;
//
//    @Autowired
//    private TaskRepository taskRepository;
//    @Scheduled(fixedDelay = Constants.SCHEDULE_LEADERBOARD_CHECKER) // Run every 10 seconds
//    public void runMethod() {
//
//
//        for (Hackathon hackathon: hackathonRepository.findAll()){
//            if (CheckOnGoingHackathon.isHackathonGoingOn(hackathon)){
//                System.out.println("Hackathon going on");
//
//                List<User> hackathonUsers = hackathon.getUsers().stream().collect(toList());
////                eventPublisher.publishEvent(new OnLeaderboardCheckEvent(hackathonUsers, hackathon.getTasks().stream().collect(toList()), hackathon));
//
//            }
//        }
//    }
//}
