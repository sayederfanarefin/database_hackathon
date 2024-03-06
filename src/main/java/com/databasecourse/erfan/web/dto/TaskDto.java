package com.databasecourse.erfan.web.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

public class TaskDto {

    private Long id;
    private String privateNotes;
    private String feedBack;
    private String title;
    private String description;
    private String outputTestQuery;
    private String outputTestMatchString;
    private Integer maxAllowedAttempts;
    private boolean logCheck;
    private boolean outPutQueryCheck;
    private String fullQueryToSearchFor;
    private String queryType;
    private boolean testCasesCheck;
    private Long customDbId;
    private List<TestCaseDto> testCases;

    @JsonManagedReference
    private List<HackathonDtoNoTask> hackathons;

    public List<HackathonDtoNoTask> getHackathons() {
        return hackathons;
    }

    public void setHackathons(List<HackathonDtoNoTask> hackathons) {
        this.hackathons = hackathons;
    }

    public TaskDto() {
        super();
    }

    public String getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(String feedBack) {
        this.feedBack = feedBack;
    }

    public String getPrivateNotes() {
        return privateNotes;
    }

//    private HackathonDto hackathon;

    public void setPrivateNotes(String privateNotes) {
        this.privateNotes = privateNotes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public HackathonDto getHackathon() {
//        return hackathon;
//    }
//
//    public void setHackathon(HackathonDto hackathon) {
//        this.hackathon = hackathon;
//    }


    public Integer getMaxAllowedAttempts() {
        return maxAllowedAttempts;
    }

    public void setMaxAllowedAttempts(Integer maxAllowedAttempts) {
        this.maxAllowedAttempts = maxAllowedAttempts;
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

    public String getOutputTestQuery() {
        return outputTestQuery;
    }

    public void setOutputTestQuery(String outputTestQuery) {
        this.outputTestQuery = outputTestQuery;
    }

    public String getOutputTestMatchString() {
        return outputTestMatchString;
    }

    public void setOutputTestMatchString(String outputTestMatchString) {
        this.outputTestMatchString = outputTestMatchString;
    }

    public boolean isLogCheck() {
        return logCheck;
    }

    public void setLogCheck(boolean logCheck) {
        this.logCheck = logCheck;
    }

    public String getFullQueryToSearchFor() {
        return fullQueryToSearchFor;
    }

    public void setFullQueryToSearchFor(String fullQueryToSearchFor) {
        this.fullQueryToSearchFor = fullQueryToSearchFor;
    }

    public List<TestCaseDto> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCaseDto> testCases) {
        this.testCases = testCases;
    }

    public boolean isOutPutQueryCheck() {
        return outPutQueryCheck;
    }

    public void setOutPutQueryCheck(boolean outPutQueryCheck) {
        this.outPutQueryCheck = outPutQueryCheck;
    }

    public boolean isTestCasesCheck() {
        return testCasesCheck;
    }

    public void setTestCasesCheck(boolean testCasesCheck) {
        this.testCasesCheck = testCasesCheck;
    }


    public Long getCustomDbId() {
        return customDbId;
    }

    public void setCustomDbId(Long customDbId) {
        this.customDbId = customDbId;
    }

    @Override
    public String toString() {
        return "TaskDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", outputTestQuery='" + outputTestQuery + '\'' +
                ", outputTestMatchString='" + outputTestMatchString + '\'' +
                ", maxAllowedAttempts=" + maxAllowedAttempts +
                ", logCheck=" + logCheck +
                ", outPutQueryCheck=" + outPutQueryCheck +
                ", fullQueryToSearchFor='" + fullQueryToSearchFor + '\'' +
                ", queryType='" + queryType + '\'' +
                ", testCasesCheck=" + testCasesCheck +
                ", customDbId=" + customDbId +
                ", testCases=" + testCases +
                '}';
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }
}
