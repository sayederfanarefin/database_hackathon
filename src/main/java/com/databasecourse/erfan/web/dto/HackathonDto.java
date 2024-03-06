package com.databasecourse.erfan.web.dto;

import java.util.List;


public class HackathonDto {

    private Long id;
    private String privateNotes;

    public String getPrivateNotes() {
        return privateNotes;
    }

    public void setPrivateNotes(String privateNotes) {
        this.privateNotes = privateNotes;
    }

    private String title;
    private String description;
    private String startDateTime;
    private String endDateTime;
    private List<Long> tasks;
    private List<Long> users;

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

    public HackathonDto() {
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

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public List<Long> getTasks() {
        return tasks;
    }

    public void setTasks(List<Long> tasks) {
        this.tasks = tasks;
    }

    public List<Long> getUsers() {
        return users;
    }

    public void setUsers(List<Long> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "HackathonDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startDateTime='" + startDateTime + '\'' +
                ", endDateTime='" + endDateTime + '\'' +
                ", tasks=" + tasks +
                ", users=" + users +
                '}';
    }
}
