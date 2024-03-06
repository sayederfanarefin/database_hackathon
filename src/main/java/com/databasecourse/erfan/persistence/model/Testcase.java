package com.databasecourse.erfan.persistence.model;

import javax.persistence.*;

@Entity
public class Testcase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "query", columnDefinition = "LONGTEXT")
    private String query;
    @Column(name = "output", columnDefinition = "LONGTEXT")
    private String output;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task task;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TestCase{" +
                "id=" + id +
                ", query='" + query + '\'' +
                ", output='" + output + '\'' +
//                ", task=" + task +
                '}';
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}