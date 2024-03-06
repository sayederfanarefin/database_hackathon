package com.databasecourse.erfan.web.dto;

import java.util.List;

public class UserDashScoreBreakDownDto {
    private String taskTitle;
    private Long taskId;
    private String timeTaken;
    private boolean complete;
    private Integer maxAttempts;
    private Integer attempts;

    private Double points;

    private List<SubmissionDto> submissions;


    public UserDashScoreBreakDownDto() {
        this.points = 0.0;
        this.attempts = 0;
        this.complete = false;
    }

    public List<SubmissionDto> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<SubmissionDto> submissions) {
        this.submissions = submissions;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public Integer getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(Integer maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "UserDashScoreBreakDownDto{" +
                "taskTitle='" + taskTitle + '\'' +
                ", taskId=" + taskId +
                ", timeTaken='" + timeTaken + '\'' +
                ", complete=" + complete +
                ", totalAttempts=" + maxAttempts +
                ", attempts=" + attempts +
                ", points=" + points +
                '}';
    }
}