package com.databasecourse.erfan.web.dto;

import com.databasecourse.erfan.validation.ValidPassword;

import javax.persistence.Column;
import java.util.List;

public class CustomdbDto {
    private Long id;


    private String privateNotes;
    private String name;
    private String description;
    private String query;

    private List<TaskDto> tasks;

    public List<TaskDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDto> tasks) {
        this.tasks = tasks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getPrivateNotes() {
        return privateNotes;
    }

    public void setPrivateNotes(String privateNotes) {
        this.privateNotes = privateNotes;
    }

    @Override
    public String toString() {
        return "CustomdbDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", query='" + query + '\'' +
                '}';
    }
}
