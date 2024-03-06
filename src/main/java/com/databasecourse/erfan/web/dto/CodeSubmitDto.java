package com.databasecourse.erfan.web.dto;

public class CodeSubmitDto {

    private Long task; // task id
    private String sql;

    private Long hackathonId;

    @Override
    public String toString() {
        return "CodeSubmitDto{" +
                "task=" + task +
                ", sql='" + sql + '\'' +
                '}';
    }

    public Long getTask() {
        return task;
    }

    public void setTask(Long task) {
        this.task = task;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Long getHackathonId() {
        return hackathonId;
    }

    public void setHackathonId(Long hackathonId) {
        this.hackathonId = hackathonId;
    }
}
