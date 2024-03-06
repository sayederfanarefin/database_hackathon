package com.databasecourse.erfan.web.dto;

import java.sql.SQLException;

public class SQLexecResults {
    public boolean status;
    public String result;

    public SQLException sqlException;

    public String error;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public SQLException getSqlException() {
        return sqlException;
    }

    public void setSqlException(SQLException sqlException) {
        this.sqlException = sqlException;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
