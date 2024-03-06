package com.databasecourse.erfan.web.dto;

import java.util.ArrayList;
import java.util.List;

public class UserDashDto {

    private Double timeLeft;
    private Integer solvedTasks;
    private Double solvedTasksPercentage;
    private Double totalPoints;
    private Integer position;

    private Long timeLeftSec;

    private List<UserDashScoreBreakDownDto> breakDownItems = new ArrayList<>();

    public Double getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Double timeLeft) {
        this.timeLeft = timeLeft;
    }


    public Integer getSolvedTasks() {
        return solvedTasks;
    }

    public void setSolvedTasks(Integer solvedTasks) {
        this.solvedTasks = solvedTasks;
    }

    public Double getSolvedTasksPercentage() {
        return solvedTasksPercentage;
    }

    public void setSolvedTasksPercentage(Double solvedTasksPercentage) {
        this.solvedTasksPercentage = solvedTasksPercentage;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public List<UserDashScoreBreakDownDto> getBreakDownItems() {
        return breakDownItems;
    }

    public void setBreakDownItems(List<UserDashScoreBreakDownDto> breakDownItems) {
        this.breakDownItems = breakDownItems;
    }

    public Long getTimeLeftSec() {
        return timeLeftSec;
    }

    public void setTimeLeftSec(Long timeLeftSec) {
        this.timeLeftSec = timeLeftSec;
    }

    public Double getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Double totalPoints) {
        this.totalPoints = totalPoints;
    }
}