package com.databasecourse.erfan.persistence.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @Column(name="description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(columnDefinition = "LONGTEXT")
    private String feedBack;
    @Column(columnDefinition = "LONGTEXT")
    private String privateNotes;
    private String queryType;

    private boolean logCheck = false;
    private boolean independentDBCheck= false;

    private boolean testCasesCheck= false;

    private Integer maxAllowedAttempts;

    @Column(name="output_test_query", columnDefinition = "LONGTEXT")
    private String outputTestQuery;

    @Column(name="output_test_match_string", columnDefinition = "LONGTEXT")
    private String outputTestMatchString;

//    @ManyToMany(mappedBy = "tasks")
    @ManyToMany(mappedBy = "tasks")
    private Collection<Hackathon> hackathons;

    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "Customdb_id")
    private Customdb customdb;


    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private Collection<Usertask> userTasks;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private Collection<Testcase> testCases;


    private boolean outPutQueryCheck;
    @Column(columnDefinition = "LONGTEXT")
    private String fullQueryToSearchFor;

    @PreRemove
    private void removeTasks() {
        customdb = null;
//        hackathons.clear();
    }

    public String getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(String feedBack) {
        this.feedBack = feedBack;
    }

    public Task() {
        super();
    }

    public boolean isOutPutQueryCheck() {
        return outPutQueryCheck;
    }

    public void setOutPutQueryCheck(boolean outPutQueryCheck) {
        this.outPutQueryCheck = outPutQueryCheck;
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


    public Collection<Hackathon> getHackathons() {
        return hackathons;
    }

    public void setHackathons(Collection<Hackathon> hackathons) {
        this.hackathons = hackathons;
    }


    public Collection<Usertask> getUserTasks() {
        return userTasks;
    }

    public void setUserTasks(Collection<Usertask> userTasks) {
        this.userTasks = userTasks;
    }

    public Collection<Testcase> getTestCases() {
        return testCases;
    }

    public void setTestCases(Collection<Testcase> testCases) {
        this.testCases = testCases;
    }

    public String getFullQueryToSearchFor() {
        return fullQueryToSearchFor;
    }

    public void setFullQueryToSearchFor(String fullQueryToSearchFor) {
        this.fullQueryToSearchFor = fullQueryToSearchFor;
    }

    public boolean isLogCheck() {
        return logCheck;
    }

    public void setLogCheck(boolean logCheck) {
        this.logCheck = logCheck;
    }

    public Integer getMaxAllowedAttempts() {
        return maxAllowedAttempts;
    }

    public void setMaxAllowedAttempts(Integer maxAllowedAttempts) {
        this.maxAllowedAttempts = maxAllowedAttempts;
    }

    public boolean isIndependentDBCheck() {
        return independentDBCheck;
    }

    public void setIndependentDBCheck(boolean independentDBCheck) {
        this.independentDBCheck = independentDBCheck;
    }

    public Customdb getCustomdb() {
        return customdb;
    }

    public void setCustomdb(Customdb customdb) {
        this.customdb = customdb;
    }

    public boolean isTestCasesCheck() {
        return testCasesCheck;
    }

    public void setTestCasesCheck(boolean testCasesCheck) {
        this.testCasesCheck = testCasesCheck;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", queryType='" + queryType + '\'' +
                ", logCheck=" + logCheck +
                ", independentDBCheck=" + independentDBCheck +
                ", testCasesCheck=" + testCasesCheck +
                ", maxAllowedAttempts=" + maxAllowedAttempts +
                ", outputTestQuery='" + outputTestQuery + '\'' +
                ", outputTestMatchString='" + outputTestMatchString + '\'' +

                ", customdb=" + customdb +
                ", userTasks=" + userTasks +
                ", testCases=" + testCases +
                ", outPutQueryCheck=" + outPutQueryCheck +
                ", fullQueryToSearchFor='" + fullQueryToSearchFor + '\'' +
                '}';
    }

    public String getPrivateNotes() {
        return privateNotes;
    }

    public void setPrivateNotes(String privateNotes) {
        this.privateNotes = privateNotes;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }
}
