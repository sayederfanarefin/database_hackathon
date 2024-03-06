package com.databasecourse.erfan.persistence.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;


@Entity
public class Usertask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    private Long hackathonId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task task;

    public LocalDateTime getAttemptStartTime() {
        return attemptStartTime;
    }

    public void setAttemptStartTime(LocalDateTime attemptStartTime) {
        this.attemptStartTime = attemptStartTime;
    }

    private LocalDateTime attemptStartTime;
    private boolean complete;
    private boolean hasStarted;

    private Integer numberOfAttempts;

    private LocalDateTime completeDate;


    private boolean graded;

    @OneToMany(mappedBy = "usertask", cascade = CascadeType.ALL)
    private Collection<Submission> submissions;

    @PreRemove
    private void removeUsertask() {
        user = null;
    }


    public Usertask() {
        super();
        this.complete = false;
        this.graded = false;
        this.numberOfAttempts = 0;
    }

    public Collection<Submission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(Collection<Submission> submissions) {
        this.submissions = submissions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public LocalDateTime getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(LocalDateTime completeDate) {
        this.completeDate = completeDate;
    }


    public boolean isGraded() {
        return graded;
    }

    public void setGraded(boolean graded) {
        this.graded = graded;
    }

    public Long getHackathonId() {
        return hackathonId;
    }

    public void setHackathonId(Long hackathonId) {
        this.hackathonId = hackathonId;
    }



    public Integer getNumberOfAttempts() {
        return numberOfAttempts;
    }

    public void setNumberOfAttempts(Integer numberOfAttempts) {
        this.numberOfAttempts = numberOfAttempts;
    }

    @Override
    public String toString() {
        return "UserTasks{" +
                "id=" + id +
                ", user=" + user.getEmail() +
                ", hackathonId=" + hackathonId +
//                ", task=" + task +
                ", complete=" + complete +
                ", numberOfAttempts=" + numberOfAttempts +
                ", completeDate=" + completeDate +
                ", graded=" + graded +
                '}';
    }

    public boolean isHasStarted() {
        return hasStarted;
    }

    public void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }
}
