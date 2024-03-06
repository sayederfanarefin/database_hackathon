package com.databasecourse.erfan.web.dto;


public class ShowUserDBTableColumnDto {

    public String columnName;
    public String dataType;
    public boolean isNullable;
    public boolean isPrimaryKey;


    @Override
    public String toString() {
        return "ShowUserDBTableColumnDto{" +
                "columnName='" + columnName + '\'' +
                ", dataType='" + dataType + '\'' +
                ", isNullable=" + isNullable +
                ", isPrimaryKey=" + isPrimaryKey +
                '}';
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public void setNullable(boolean nullable) {
        isNullable = nullable;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public ShowUserDBTableColumnDto(String columnName, String dataType, boolean isNullable, boolean isPrimaryKey) {
        this.columnName = columnName;
        this.dataType = dataType;
        this.isNullable = isNullable;
        this.isPrimaryKey = isPrimaryKey;
    }
}
