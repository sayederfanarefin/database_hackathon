package com.databasecourse.erfan.web.dto;

public class TestCaseDto {

    private String query;
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

    @Override
    public String toString() {
        return "TestCase{" +
                "query='" + query + '\'' +
                ", output='" + output + '\'' +
                '}';
    }
}
