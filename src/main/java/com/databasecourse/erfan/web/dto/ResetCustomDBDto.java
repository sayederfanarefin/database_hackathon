package com.databasecourse.erfan.web.dto;

public class ResetCustomDBDto {

    Long dbId;

    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    @Override
    public String toString() {
        return "ResetCustomDBDto{" +
                "dbId=" + dbId +
                '}';
    }
}
