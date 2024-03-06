package com.databasecourse.erfan.web.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.StringJoiner;

public class SQLResultToString {

    public static String resultSetToString(ResultSet resultSet, boolean needHeader) throws SQLException {
        StringBuilder resultString = new StringBuilder();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Append column names to the string
        StringJoiner headerJoiner = new StringJoiner(", ");
        for (int i = 1; i <= columnCount; i++) {
            headerJoiner.add(metaData.getColumnName(i));
        }
        if (needHeader){
            resultString.append(headerJoiner).append(System.lineSeparator());
        }

        // Append row data to the string
        while (resultSet.next()) {
            StringJoiner rowJoiner = new StringJoiner(", ");
            for (int i = 1; i <= columnCount; i++) {
                rowJoiner.add(resultSet.getString(i));
            }
            resultString.append(rowJoiner).append(System.lineSeparator());
        }

        return resultString.toString();
    }
}
