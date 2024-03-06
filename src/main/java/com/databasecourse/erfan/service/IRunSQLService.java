package com.databasecourse.erfan.service;


import com.databasecourse.erfan.web.dto.ShowUserDBDto;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IRunSQLService {

    String cleanSQLString(String sql);

    List<String> splitSQLQueries(String sql);

    int runQuery(String query);

    String runQueryExec(String query);
    String runQueryExecThrowException(String query) throws SQLException;

    ResultSet runQueryExecRS(String query);

    ShowUserDBDto getUserDBStat(String dbName);

    ResultSet runQueryExecWithUserDb(String query, String dbName);

    void backupDB  (String dbName, String backupPath) throws IOException;
    void restoreDB (String dbName, String restorePath);

    List<String> getTableNames(String dbName);

}
