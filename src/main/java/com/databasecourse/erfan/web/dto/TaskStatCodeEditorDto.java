package com.databasecourse.erfan.web.dto;

public class TaskStatCodeEditorDto {
    private boolean taskStatus;
    private Integer attemptsLeft;
    private Integer maxAllowedAttempts;
    private Double overAllProgress;
    private Double averageAttemptsAllUsers;

    public boolean isTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(boolean taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Integer getAttemptsLeft() {
        return attemptsLeft;
    }

    public void setAttemptsLeft(Integer attemptsLeft) {
        this.attemptsLeft = attemptsLeft;
    }

    public Integer getMaxAllowedAttempts() {
        return maxAllowedAttempts;
    }

    public void setMaxAllowedAttempts(Integer maxAllowedAttempts) {
        this.maxAllowedAttempts = maxAllowedAttempts;
    }

    public Double getOverAllProgress() {
        return overAllProgress;
    }

    public void setOverAllProgress(Double overAllProgress) {
        this.overAllProgress = overAllProgress;
    }

    public Double getAverageAttemptsAllUsers() {
        return averageAttemptsAllUsers;
    }

    public void setAverageAttemptsAllUsers(Double averageAttemptsAllUsers) {
        this.averageAttemptsAllUsers = averageAttemptsAllUsers;
    }

    @Override
    public String toString() {
        return "TaskStatCodeEditorDto{" +
                "taskStatus=" + taskStatus +
                ", attemptsLeft=" + attemptsLeft +
                ", maxAllowedAttempts=" + maxAllowedAttempts +
                ", overAllProgress=" + overAllProgress +
                ", averageAttemptsAllUsers=" + averageAttemptsAllUsers +
                '}';
    }
}
