package com.databasecourse.erfan.web.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.databasecourse.erfan.Constants.DATE_TIME_DISPLAY_PATTERN;


public class HackathonDtoDisplay {

    private Long id;
    private String title;
    private String privateNotes;

    public String getPrivateNotes() {
        return privateNotes;
    }

    public void setPrivateNotes(String privateNotes) {
        this.privateNotes = privateNotes;
    }

    private String description;
    private String startDateTime;
    private String endDateTime;
    @JsonBackReference
    private List<TaskDto> tasks;
    private List<UserDisplayDto> users;

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

    public HackathonDtoDisplay() {
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

//    public void setStartDateTime(String startDateTime) {
//        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
//        LocalDateTime parsedDateTime = LocalDateTime.parse(startDateTime, formatter);
//        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern(DATE_TIME_DISPLAY_PATTERN);
//
//        this.startDateTime = parsedDateTime.format(formatter2);
//    }



    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }
    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

//    public void setEndDateTime(String endDateTime) {
//        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
//        LocalDateTime parsedDateTime = LocalDateTime.parse(endDateTime, formatter);
//        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern(DATE_TIME_DISPLAY_PATTERN);
//
//        this.endDateTime = parsedDateTime.format(formatter2);
//    }

    public List<TaskDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDto> tasks) {
        this.tasks = tasks;
    }

    public List<UserDisplayDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDisplayDto> users) {
        this.users = users;
    }
}
