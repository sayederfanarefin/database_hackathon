package com.databasecourse.erfan.registration.listener;

import com.databasecourse.erfan.persistence.dao.*;
import com.databasecourse.erfan.persistence.model.*;
import com.databasecourse.erfan.registration.UserTaskUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.databasecourse.erfan.Constants.BONUS_SECOND_MULTIPLIER;
import static com.databasecourse.erfan.Constants.POINT_FOR_COMPLETING_TASK;

@Component
@Transactional
public class UserTaskUpdateListener implements ApplicationListener<UserTaskUpdateEvent> {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HackathonRepository hackathonRepository;


    @Autowired
    private UsertaskRepository userTasksRepository;

    @Autowired
    private LeaderboardRepository leaderboardRepository;
    @Autowired
    private LeaderelementRepository leaderelementRepository;


    // API

    @Override
    public void onApplicationEvent(final UserTaskUpdateEvent event) {
        this.run(event);
    }

    private void run(UserTaskUpdateEvent event){
        System.out.println("--------- iam here ---------");
        Hackathon hackathon = hackathonRepository.findById(event.getHackathon().getId()).get();
        Leaderboard leaderboard = hackathon.getLeaderboard();
        List<Leaderelement> leaderboardElements = leaderboard.getLeaderelements();

        LocalDateTime hackathonStartTime = event.getHackathon().getStartDateTime();
        LocalDateTime hackathonEndTime = event.getHackathon().getEndDateTime();
        Duration duration = Duration.between(hackathonStartTime, hackathonEndTime);
        long totalHackathonDuration = duration.getSeconds();
        int totalTask = hackathon.getTasks().size();
        long averageTimePerTask = totalHackathonDuration/totalTask;


        for (User user: event.getHackathon().getUsers()) {

            System.out.println("--------- i am here user ---------");
            System.out.println(user.getDatabaseUserName());

            double totalUserPoints = 0.0;
            for (Usertask userTasks: userTasksRepository.findByUser_IdAndHackathonId(user.getId(), hackathon.getId())){
                Task task = userTasks.getTask();

                System.out.println("--------- userTasks ---------");
                System.out.println("task" + task.toString());
                System.out.println("userTasks" + userTasks);

                double pointsForTask = 0.0;
                if (userTasks.isComplete() && !userTasks.isGraded()) {
                    pointsForTask = pointsForTask + POINT_FOR_COMPLETING_TASK;
                    long durationOfTaskSeconds = Duration.between(hackathonStartTime, userTasks.getCompleteDate()).getSeconds();
                    if (durationOfTaskSeconds <= averageTimePerTask){
                        // bonuse point
                        pointsForTask = pointsForTask + ((averageTimePerTask-durationOfTaskSeconds) * BONUS_SECOND_MULTIPLIER);
                        System.out.println(userTasks);
                        System.out.println("averageTimePerTask"+averageTimePerTask+"durationOfTaskSeconds"+ durationOfTaskSeconds);
                        System.out.println("pointsForTask"+pointsForTask);

                        userTasks.setGraded(true);
                        userTasks.setTask(task);
                        userTasksRepository.save(userTasks);
                    }
                }
                totalUserPoints = totalUserPoints + pointsForTask;
                System.out.println(userTasks);
                System.out.println("calculating totalUserPoints: " + totalUserPoints);
            }
            System.out.println("TotalUserPoints: " + totalUserPoints);
            Leaderelement leaderelement = leaderelementRepository.findByUserIdOriginalAndLeaderboard_Hackathon_Id(user.getId(), hackathon.getId());
            leaderelement.setPoints(totalUserPoints);

            leaderelementRepository.save(leaderelement);


//            for (Task task : userRepository.findById(user.getId()).get().getTasks()) {
//
//                double pointsForTask = 0.0;
//                if (task.isComplete()) {
//                    pointsForTask = pointsForTask + POINT_FOR_COMPLETING_TASK;
//
//
//                    long durationOfTaskSeconds = Duration.between(hackathonStartTime, task.getCompleteDate()).getSeconds();
//                    if (durationOfTaskSeconds <= averageTimePerTask){
//                        // bonuse point
//
//                        pointsForTask = pointsForTask + ((averageTimePerTask-durationOfTaskSeconds) * BONUS_SECOND_MULTIPLIER);
//                    }
//                }
//
//                totalUserPoints = totalUserPoints + pointsForTask;
//            }
//
//            Leaderelement leaderelement = leaderelementRepository.findByUserIdOriginalAndLeaderboard_Hackathon_Id(user.getId(), hackathon.getId()).get(0);
//            leaderelement.setPoints(totalUserPoints);
//
//            leaderelementRepository.save(leaderelement);

        }
    }


}
