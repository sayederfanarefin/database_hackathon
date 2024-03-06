package com.databasecourse.erfan.persistence.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Customdb {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(columnDefinition = "LONGTEXT")
    private String privateNotes;

    @Column(name = "query", columnDefinition = "LONGTEXT")
    private String query;

    @OneToMany(mappedBy = "customdb", cascade= CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();


    @PreRemove
    private void removeCustomdb() {
        tasks.clear();
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

    public String getQuery() {
        return query;
    }

    public void setQuery(String tablesCreateQuery) {
        this.query = tablesCreateQuery;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getDescription() {
        return description;
    }

    public String getPrivateNotes() {
        return privateNotes;
    }

    public void setPrivateNotes(String privateNotes) {
        this.privateNotes = privateNotes;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}