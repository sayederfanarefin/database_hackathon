package com.databasecourse.erfan.service;

import com.databasecourse.erfan.persistence.dao.*;
import com.databasecourse.erfan.persistence.dtoConverters.SubmissionModelConverter;
import com.databasecourse.erfan.persistence.model.*;
import com.databasecourse.erfan.web.dto.UserDashScoreBreakDownDto;
import com.databasecourse.erfan.web.util.PointCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class UserScoreService implements IUserScoreService {

    @Autowired
    private UsertaskRepository userTasksRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SubmissionModelConverter submissionModelConverter;


    @Override
    public List<UserDashScoreBreakDownDto> getUserDashScoreBreakDown(User user, Hackathon hackathon){
        List<UserDashScoreBreakDownDto> userDashScoreBreakDownDtos = new ArrayList<>();

        for ( Usertask userTasks: userTasksRepository.findByUser_IdAndHackathonId(user.getId(), hackathon.getId())){

            UserDashScoreBreakDownDto userDashScoreBreakDownDto = new UserDashScoreBreakDownDto();
            userDashScoreBreakDownDto.setTaskId(userTasks.getTask().getId());
            userDashScoreBreakDownDto.setComplete(userTasks.isComplete());
            userDashScoreBreakDownDto.setSubmissions(submissionModelConverter.convertList(userTasks.getSubmissions().stream().collect(toList())));

            if (userTasks.getCompleteDate() == null){
                userDashScoreBreakDownDto.setTimeTaken("---");
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss, MM/dd/yyyy");
                userDashScoreBreakDownDto.setTimeTaken(userTasks.getCompleteDate().format(formatter));
            }
            userDashScoreBreakDownDto.setMaxAttempts(userTasks.getNumberOfAttempts());
            userDashScoreBreakDownDto.setTaskTitle(userTasks.getTask().getTitle());
            Task task = taskRepository.findById(userTasks.getTask().getId()).get();
//            Task task = taskService.getTaskFull(userTasks.getTask().getId());
            if (userDashScoreBreakDownDto.isComplete()){
                double calculatedPoints = PointCalculator.calculatePoints(userTasks);
                userDashScoreBreakDownDto.setPoints(Double.valueOf(calculatedPoints));
            }

            userDashScoreBreakDownDto.setAttempts(userTasks.getNumberOfAttempts());
            userDashScoreBreakDownDto.setMaxAttempts(task.getMaxAllowedAttempts());
            userDashScoreBreakDownDtos.add(userDashScoreBreakDownDto);

        }
        return userDashScoreBreakDownDtos;
    }


}
