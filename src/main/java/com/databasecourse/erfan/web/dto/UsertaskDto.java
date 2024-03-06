package com.databasecourse.erfan.web.dto;

import com.databasecourse.erfan.persistence.model.Submission;
import com.databasecourse.erfan.persistence.model.Task;
import com.databasecourse.erfan.persistence.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

public class UsertaskDto {
    private Long id;
    private Long hackathonId;
    private TaskDto task;
    private boolean complete;
    private Integer numberOfAttempts;
    private LocalDateTime completeDate;

    private boolean hasStarted;

    public boolean isHasStarted() {
        return hasStarted;
    }

    public void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    private String attemptStartTime;

    public String getAttemptStartTime() {
        return attemptStartTime;
    }

    public void setAttemptStartTime(String attemptStartTime) {
        this.attemptStartTime = attemptStartTime;
    }

    private boolean graded;
    private Collection<SubmissionDto> submissions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHackathonId() {
        return hackathonId;
    }

    public void setHackathonId(Long hackathonId) {
        this.hackathonId = hackathonId;
    }

    public TaskDto getTask() {
        return task;
    }

    public void setTask(TaskDto task) {
        this.task = task;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public Integer getNumberOfAttempts() {
        return numberOfAttempts;
    }

    public void setNumberOfAttempts(Integer numberOfAttempts) {
        this.numberOfAttempts = numberOfAttempts;
    }

    public LocalDateTime getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(LocalDateTime completeDate) {
        this.completeDate = completeDate;
    }

    public boolean isGraded() {
        return graded;
    }

    public void setGraded(boolean graded) {
        this.graded = graded;
    }

    public Collection<SubmissionDto> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(Collection<SubmissionDto> submissions) {
        this.submissions = submissions;
    }
}
