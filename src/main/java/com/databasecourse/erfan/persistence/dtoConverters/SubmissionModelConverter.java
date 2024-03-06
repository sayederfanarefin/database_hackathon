package com.databasecourse.erfan.persistence.dtoConverters;

import com.databasecourse.erfan.persistence.dao.UsertaskRepository;
import com.databasecourse.erfan.persistence.model.Submission;
import com.databasecourse.erfan.persistence.model.User;
import com.databasecourse.erfan.persistence.model.Usertask;
import com.databasecourse.erfan.web.dto.SubmissionDto;
import com.databasecourse.erfan.web.dto.UserDisplayDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubmissionModelConverter {

    @Autowired
    private UsertaskRepository usertaskRepository;

    public SubmissionDto convert(Submission submission){
        SubmissionDto submissionDto = new SubmissionDto();
        submissionDto.setId(submission.getId());
        submissionDto.setPassed(submission.isPassed());
        submissionDto.setSubmissionTime(submission.getSubmissionTime());
        submissionDto.setSubmittedQuery(submission.getSubmittedQuery());

        return submissionDto;
    }


    public Submission convert(SubmissionDto submissionDto){
        Submission submission = new Submission();
        submission.setId(submissionDto.getId());
        submission.setSubmissionTime(submissionDto.getSubmissionTime());
        submission.setSubmittedQuery(submissionDto.getSubmittedQuery());
        submission.setPassed(submissionDto.isPassed());

        Usertask usertask = usertaskRepository.findById(submissionDto.getUsertaskId()).get();
        submission.setUserTasks(usertask);

        return submission;
    }
    public List<SubmissionDto> convertList(List<Submission> submissions){
        List<SubmissionDto> submissionDtos = new ArrayList<>();
        for (Submission submission : submissions) {
            submissionDtos.add(convert(submission));
        }
        return submissionDtos;
    }

}
