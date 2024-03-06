package com.databasecourse.erfan.registration.listener;

import com.databasecourse.erfan.Constants;
import com.databasecourse.erfan.persistence.dao.UserRepository;
import com.databasecourse.erfan.persistence.dao.UsertaskRepository;
import com.databasecourse.erfan.persistence.model.User;
import com.databasecourse.erfan.persistence.model.Usertask;
import com.databasecourse.erfan.registration.OnLeaderboardCheckEvent;
import com.databasecourse.erfan.service.ReadMySQLLogService;
import com.databasecourse.erfan.service.RunSQLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Component
@Transactional
public class LeaderboardCheckListener implements ApplicationListener<OnLeaderboardCheckEvent> {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsertaskRepository userTasksRepository;

    @Autowired
    private RunSQLService runSQLService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private ReadMySQLLogService readMySQLLogService;

    // API

    @Override
    public void onApplicationEvent(final OnLeaderboardCheckEvent event) {
        this.run(event);
    }

    private void run(OnLeaderboardCheckEvent event){

            for (User user: event.getHackathonUsers()){

                User userRetrived = userRepository.findById(user.getId()).get();
                String dbName = userRetrived.getDatabaseName();

                for (Usertask userTasks: userTasksRepository.findByUser_IdAndHackathonId(userRetrived.getId(), event.getHackathon().getId())){
                    if (!userTasks.isComplete()){

                        System.out.println("Inside leader board check listener if block (!userTasks.isComplete())");

                        boolean taskOutputCheck = false;
                        boolean taskQueryCheck = false;


                        if (userTasks.getTask().isOutPutQueryCheck()){
                            System.out.println("Output query checking enabled for "+userTasks.getTask().toString() );
                            // needs output query checking
                            taskOutputCheck = checkTaskInDB(userTasks.getTask().getOutputTestQuery(), userTasks.getTask().getOutputTestMatchString(), dbName);

                        } else {
                            System.out.println("Output query checking not enabled for "+userTasks.getTask().toString() );

                            taskOutputCheck = true;
                        }



                        if (userTasks.getTask().isLogCheck()){
                            System.out.println("log checking enabled for "+userTasks.getTask().toString() );

                            // needs log table checking
                            taskQueryCheck = readMySQLLogService.checkLogForUser(userTasks.getUser().getDatabaseUserName(), userTasks.getTask().getFullQueryToSearchFor());
                        }
                        else {
                            System.out.println("Output query checking not enabled for "+userTasks.getTask().toString() );

                            taskQueryCheck = true;
                        }

                        if (taskOutputCheck && taskQueryCheck) {
                            userTasks.setComplete(true);
                            userTasks.setCompleteDate(LocalDateTime.now());
                            userTasksRepository.save(userTasks);
//                            eventPublisher.publishEvent(new UserTaskUpdateEvent(userRetrived, userTasks.getTask(), event.getHackathon()));
                        }

                    } else {
                        System.out.println("Inside leader boarc check listner else (!userTasks.isComplete())");
                    }
                }

            }
    }

    private boolean checkTaskInDB(String outputQuery, String outputString, String dbName){
        // make a call to runSQLService.runQuery();

       outputQuery = outputQuery.replace(Constants.DATABASE_PLACEHOLDER, dbName);
       String result = runSQLService.runQueryExec(outputQuery);

        if (result != null){

            System.out.println("---------- query checker ---------");
            System.out.println("------ outputQuery -----");
            System.out.println(outputQuery);
            System.out.println("------ outputString -----");
            System.out.println(outputString);
            System.out.println("------ resultSet -----");
            System.out.println(result);

        }

//        String preparedResult = replaceSpaces(result);
//        String preparedOutput = replaceSpaces(outputString);

        return result != null && result.equals(outputString);

    }

//    public static String replaceSpaces(String inputString) {
//        if (inputString != null){
//            return inputString.replaceAll("\\s", "");
//        } else {
//            return inputString;
//        }
//
//    }


}
