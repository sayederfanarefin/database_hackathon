package com.databasecourse.erfan.service;

import com.databasecourse.erfan.web.util.SQLResultToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;

import static com.databasecourse.erfan.Constants.*;

@Service
public class ReadMySQLLogService implements IReadMySQLLogServie{

    @Value("${spring.datasource.username}")
    private String rootUserName;

    @Value("${spring.datasource.password}")
    private String rootUserPassword;

    @Value("${spring.datasource.url}")
    private String dbURL;

    @Autowired
    private RunSQLService runSQLService;

    public String readLogForUser(String mySqlUserName){
        // todo fix hackathon start time to reduce query time

        String dbMysqlUrl = "jdbc:mysql://" + DB_URL + ":" + DB_PORT + "/" + LOG_DB ;

        System.out.println("------ running read log query -------");

        try (Connection con = DriverManager.getConnection(dbMysqlUrl, rootUserName, rootUserPassword)){
            Statement st = con.createStatement();
            String queryUserLogs = "SELECT * FROM `" + LOG_TABLE + "` WHERE 'command_type'='query' AND 'user_host' LIKE '%" + mySqlUserName + "%';";

            ResultSet rs = st.executeQuery(queryUserLogs);
            String rss = SQLResultToString.resultSetToString(rs, false);
            System.out.println(rss);
            rs.close();



        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("------ ending read log query -------");


         return "";
    }


    public boolean checkLogForUser(String mySqlUserName, String searchString){
        // todo fix hackathon start time to reduce query time

        String dbMysqlUrl = "jdbc:mysql://" + DB_URL + ":" + DB_PORT + "/" + LOG_DB ;

        System.out.println("------ checking log for user -------");

        try (Connection con = DriverManager.getConnection(dbMysqlUrl, rootUserName, rootUserPassword)){
            Statement st = con.createStatement();
            String queryUserLogs = "SELECT * FROM `" + LOG_TABLE + "` WHERE 'command_type'='query' AND 'user_host' LIKE '%" + mySqlUserName + "%';";

            ResultSet resultSet = st.executeQuery(queryUserLogs);
            boolean found = false;

            while (resultSet.next()) {
                // Get the value from the current row and column (assuming 'column_name' is the column you want to check)
                String value = resultSet.getString("column_name");

                // Perform the comparison to check if the string is present
                if (value.equals(searchString)) {
                    found = true;
                    break; // If you only want to check if it's present once, you can break out of the loop here.
                }
            }
            return found;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("------ ending read log query -------");


        return false;
    }


}
