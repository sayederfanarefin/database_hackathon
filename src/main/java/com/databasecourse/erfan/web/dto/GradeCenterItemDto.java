package com.databasecourse.erfan.web.dto;

import java.util.List;

public class GradeCenterItemDto {
    private List<UserDashScoreBreakDownDto> userDashScoreBreakDownDtoList;
    private UserDisplayDto user;

    private double totalPoints;

    public double getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(double totalPoints) {
        this.totalPoints = totalPoints;
    }

    public List<UserDashScoreBreakDownDto> getUserDashScoreBreakDownDtoList() {
        return userDashScoreBreakDownDtoList;
    }

    public void setUserDashScoreBreakDownDtoList(List<UserDashScoreBreakDownDto> userDashScoreBreakDownDtoList) {
        this.userDashScoreBreakDownDtoList = userDashScoreBreakDownDtoList;
    }

    public UserDisplayDto getUser() {
        return user;
    }

    public void setUser(UserDisplayDto user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "GradeCenterItemDto{" +
                "userDashScoreBreakDownDtoList=" + userDashScoreBreakDownDtoList +
                ", user=" + user +
                '}';
    }
}
