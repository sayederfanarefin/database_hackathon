package com.databasecourse.erfan.web.dto;

public class StartDateSubmitDto {

    private Long task; // task id

    private Long hackathonId;


    public Long getTask() {
        return task;
    }

    public void setTask(Long task) {
        this.task = task;
    }

    public Long getHackathonId() {
        return hackathonId;
    }

    public void setHackathonId(Long hackathonId) {
        this.hackathonId = hackathonId;
    }
}
