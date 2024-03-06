package com.databasecourse.erfan.web.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomDBTableDto {
    String tableName;
    List<Map<String, Object>> tableData = new ArrayList<>();

    @Override
    public String toString() {
        return "CustomDBTableDto{" +
                "tableName='" + tableName + '\'' +
                ", tableData=" + tableData +
                '}';
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Map<String, Object>> getTableData() {
        return tableData;
    }

    public void setTableData(List<Map<String, Object>> tableData) {
        this.tableData = tableData;
    }
}
