package com.databasecourse.erfan.web.dto;

public class CustomDBTestQueryDto {

    private Long dbId;
    private String testQuery;
    private String testQueryResults;

    public String getTestQuery() {
        return testQuery;
    }

    public void setTestQuery(String testQuery) {
        this.testQuery = testQuery;
    }

    public String getTestQueryResults() {
        return testQueryResults;
    }

    public void setTestQueryResults(String testQueryResults) {
        this.testQueryResults = testQueryResults;
    }

    public CustomDBTestQueryDto() {
        super();

    }

    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    @Override
    public String toString() {
        return "CustomDBTestQueryDto{" +
                "dbId=" + dbId +
                ", testQuery='" + testQuery + '\'' +
                ", testQueryResults='" + testQueryResults + '\'' +
                '}';
    }
}
