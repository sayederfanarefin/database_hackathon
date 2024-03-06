package com.databasecourse.erfan.web.dto;

import com.databasecourse.erfan.persistence.model.Leaderelement;

import java.util.List;

public class LeaderBoardRestResponse {

    public int timLeftYear;
    public int timLeftMonth;
    public int timLeftDay;
    public int timLeftHour;
    public int timLeftMin;
    public int timLeftSec;

    public Long activeUserId;

    List<LeaderelementDto> data;

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

    public List<LeaderelementDto> getData() {
        return data;
    }

    public void setData(List<LeaderelementDto> data) {
        this.data = data;
    }
}
