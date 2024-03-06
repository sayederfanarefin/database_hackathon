package com.databasecourse.erfan.web.dto;

import java.util.List;


public class HackathonDtoNoTask {

    private Long id;
    private String title;
    private String description;
    private String startDateTime;
    private String endDateTime;

    private boolean leaderBoardEnabled;
    private boolean generalBoardEnabled;

    public boolean isLeaderBoardEnabled() {
        return leaderBoardEnabled;
    }

    public void setLeaderBoardEnabled(boolean leaderBoardEnabled) {
        this.leaderBoardEnabled = leaderBoardEnabled;
    }

    public boolean isGeneralBoardEnabled() {
        return generalBoardEnabled;
    }

    public void setGeneralBoardEnabled(boolean generalBoardEnabled) {
        this.generalBoardEnabled = generalBoardEnabled;
    }

    public HackathonDtoNoTask() {
        super();
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }



    public String getStartDateTime() {
        return startDateTime;
    }


    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }
    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    @Override
    public String toString() {
        return "HackathonDtoNoTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startDateTime='" + startDateTime + '\'' +
                ", endDateTime='" + endDateTime + '\'' +
                '}';
    }
}
