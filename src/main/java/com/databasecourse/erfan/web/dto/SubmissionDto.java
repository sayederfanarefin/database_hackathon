package com.databasecourse.erfan.web.dto;

import com.databasecourse.erfan.persistence.model.Usertask;

import javax.persistence.*;
import java.time.LocalDateTime;

public class SubmissionDto {


    private Long id;
    private Long usertaskId;

    private LocalDateTime submissionTime;

    private String submittedQuery;
    private boolean passed;

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

    @Override
    public String toString() {
        return "SubmissionDto{" +
                "id=" + id +
                ", usertaskId=" + usertaskId +
                ", submissionTime=" + submissionTime +
                ", submittedQuery='" + submittedQuery + '\'' +
                ", passed=" + passed +
                '}';
    }

    public Long getUsertaskId() {
        return usertaskId;
    }

    public SubmissionDto() {
    }

    public void setUsertaskId(Long usertaskId) {
        this.usertaskId = usertaskId;
    }

}
