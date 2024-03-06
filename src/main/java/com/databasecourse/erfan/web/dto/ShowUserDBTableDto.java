package com.databasecourse.erfan.web.dto;


import java.util.ArrayList;
import java.util.List;

public class ShowUserDBTableDto {

    private String tableName;

    private List<ShowUserDBTableColumnDto> columns = new ArrayList<>();


    public ShowUserDBTableDto(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<ShowUserDBTableColumnDto> getColumns() {
        return columns;
    }

    public void setColumns(List<ShowUserDBTableColumnDto> columns) {
        this.columns = columns;
    }

    @Override
    public String toString() {
        return "ShowUserDBTableDto{" +
                "tableName='" + tableName + '\'' +
                ", columns=" + columns +
                '}';
    }
}
