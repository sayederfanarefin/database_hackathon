package com.databasecourse.erfan.persistence.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Submission {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime submissionTime;

    @Column(columnDefinition = "LONGTEXT")
    private String submittedQuery;

    private boolean passed;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usertask_id", referencedColumnName = "id")
    private Usertask usertask;

    public Submission(LocalDateTime submissionTime, String submittedQuery, boolean passed, Usertask usertask) {
        this.submissionTime = submissionTime;
        this.submittedQuery = submittedQuery;
        this.passed = passed;
        this.usertask = usertask;
    }

    public Submission() {
    }

    @Override
    public String toString() {
        return "Submission{" +
                "id=" + id +
                ", submissionTime=" + submissionTime +
                ", submittedQuery='" + submittedQuery + '\'' +
                ", passed=" + passed +
                ", usertasks=" + usertask +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(LocalDateTime submissionTime) {
        this.submissionTime = submissionTime;
    }

    public String getSubmittedQuery() {
        return submittedQuery;
    }

    public void setSubmittedQuery(String submittedQuery) {
        this.submittedQuery = submittedQuery;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public Usertask getUserTasks() {
        return usertask;
    }

    public void setUserTasks(Usertask usertask) {
        this.usertask = usertask;
    }
}
