package com.databasecourse.erfan.persistence.dtoConverters;

import com.databasecourse.erfan.persistence.dao.UsertaskRepository;
import com.databasecourse.erfan.persistence.model.Submission;
import com.databasecourse.erfan.persistence.model.Usertask;
import com.databasecourse.erfan.web.dto.SubmissionDto;
import com.databasecourse.erfan.web.dto.UsertaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.databasecourse.erfan.Constants.DATE_TIME_DISPLAY_PATTERN;


@Service
public class UserTasksModelConverter {

    @Autowired
    private TaskDtoModelConverrter taskDtoModelConverrter;
    @Autowired
    private SubmissionModelConverter submissionModelConverter;


    public UsertaskDto convert(Usertask userTask){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_DISPLAY_PATTERN);


        UsertaskDto usertaskDto = new UsertaskDto();
        usertaskDto.setId(userTask.getId());
        usertaskDto.setComplete(userTask.isComplete());
        usertaskDto.setGraded(userTask.isGraded());
        usertaskDto.setHackathonId(userTask.getHackathonId());
        usertaskDto.setNumberOfAttempts(userTask.getNumberOfAttempts());
        usertaskDto.setCompleteDate(userTask.getCompleteDate());
        usertaskDto.setTask(taskDtoModelConverrter.convert(userTask.getTask()));
        usertaskDto.setSubmissions(submissionModelConverter.convertList(userTask.getSubmissions().stream().collect(Collectors.toList())));

        if (userTask.getAttemptStartTime() != null){
            usertaskDto.setAttemptStartTime(userTask.getAttemptStartTime().format(formatter));

        }
        usertaskDto.setHasStarted(userTask.isHasStarted());
         return usertaskDto;
    }


//    public Submission convert(SubmissionDto submissionDto){
//        Submission submission = new Submission();
//        submission.setId(submissionDto.getId());
//        submission.setSubmissionTime(submissionDto.getSubmissionTime());
//        submission.setSubmittedQuery(submissionDto.getSubmittedQuery());
//        submission.setPassed(submissionDto.isPassed());
//
//        Usertask usertask = usertaskRepository.findById(submissionDto.getUsertaskId()).get();
//        submission.setUserTasks(usertask);
//
//        return submission;
//    }
    public List<UsertaskDto> convertList(List<Usertask> userTasks){
        List<UsertaskDto> usertaskDtos = new ArrayList<>();
        for (Usertask userTask : userTasks) {
            usertaskDtos.add(convert(userTask));
        }
        return usertaskDtos;
    }

    private String keepFirst10Chars(String input) {
        if (input.length() <= 10) {
            return input;
        } else {
            return input.substring(0, 10);
        }
    }

    public List<UsertaskDto> convertListTruncated(List<Usertask> userTasks){
        List<UsertaskDto> usertaskDtos = new ArrayList<>();
        for (Usertask userTask : userTasks) {
            UsertaskDto usertaskDto = convert(userTask);
            usertaskDto.getTask().setDescription(keepFirst10Chars(usertaskDto.getTask().getDescription()) + " ... " );
            usertaskDtos.add(usertaskDto);
        }
        return usertaskDtos;
    }

}
