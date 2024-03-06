package com.databasecourse.erfan.web.dto;

import java.util.ArrayList;
import java.util.List;

public class GeneralBoardRestResponse {

    public double averageTimePerTask;
    public double averageScore;
    public double bestScore;
    public double averageAttemptsPerTask;



    public int timLeftYear;
    public int timLeftMonth;
    public int timLeftDay;
    public int timLeftHour;
    public int timLeftMin;
    public int timLeftSec;

    public Long activeUserId;

    public int userPosition;

    public double userScore;

    public List<String> teams = new ArrayList<>();

    public int getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(int userPosition) {
        this.userPosition = userPosition;
    }

    public double getUserScore() {
        return userScore;
    }

    public void setUserScore(double userScore) {
        this.userScore = userScore;
    }

    public List<String> getTeams() {
        return teams;
    }

    public void setTeams(List<String> teams) {
        this.teams = teams;
    }

    public int getTimLeftYear() {
        return timLeftYear;
    }

    public void setTimLeftYear(int timLeftYear) {
        this.timLeftYear = timLeftYear;
    }

    public int getTimLeftMonth() {
        return timLeftMonth;
    }

    public void setTimLeftMonth(int timLeftMonth) {
        this.timLeftMonth = timLeftMonth;
    }

    public int getTimLeftDay() {
        return timLeftDay;
    }

    public void setTimLeftDay(int timLeftDay) {
        this.timLeftDay = timLeftDay;
    }

    public int getTimLeftHour() {
        return timLeftHour;
    }

    public void setTimLeftHour(int timLeftHour) {
        this.timLeftHour = timLeftHour;
    }

    public int getTimLeftMin() {
        return timLeftMin;
    }

    public void setTimLeftMin(int timLeftMin) {
        this.timLeftMin = timLeftMin;
    }

    public int getTimLeftSec() {
        return timLeftSec;
    }

    public void setTimLeftSec(int timLeftSec) {
        this.timLeftSec = timLeftSec;
    }

    public Long getActiveUserId() {
        return activeUserId;
    }

    public void setActiveUserId(Long activeUserId) {
        this.activeUserId = activeUserId;
    }


    public double getAverageTimePerTask() {
        return averageTimePerTask;
    }

    public void setAverageTimePerTask(double averageTimePerTask) {
        this.averageTimePerTask = averageTimePerTask;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public double getBestScore() {
        return bestScore;
    }

    public void setBestScore(double bestScore) {
        this.bestScore = bestScore;
    }

    public double getAverageAttemptsPerTask() {
        return averageAttemptsPerTask;
    }

    public void setAverageAttemptsPerTask(double averageAttemptsPerTask) {
        this.averageAttemptsPerTask = averageAttemptsPerTask;
    }
}
