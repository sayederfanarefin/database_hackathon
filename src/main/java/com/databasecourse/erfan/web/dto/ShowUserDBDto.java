package com.databasecourse.erfan.web.dto;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;


public class ShowUserDBDto {
    private String dbName;
    private List<ShowUserDBTableDto> tables = new ArrayList<>();

    public ShowUserDBDto(String dbName) {
        this.dbName = dbName;
    }
    public String getDbName() {
        return dbName;
    }
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
    public List<ShowUserDBTableDto> getTables() {
        return tables;
    }
    public void setTables(List<ShowUserDBTableDto> tables) {
        this.tables = tables;
    }

    @Override
    public String toString() {
        return "ShowUserDBDto{" +
                "dbName='" + dbName + '\'' +
                ", tables=" + tables +
                '}';
    }
}
