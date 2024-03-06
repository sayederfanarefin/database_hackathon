package com.databasecourse.erfan.service;
import com.databasecourse.erfan.web.dto.SQLexecResults;
import com.databasecourse.erfan.web.dto.ShowUserDBDto;
import com.databasecourse.erfan.web.dto.ShowUserDBTableColumnDto;
import com.databasecourse.erfan.web.dto.ShowUserDBTableDto;
import com.databasecourse.erfan.web.util.SQLResultToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.databasecourse.erfan.Constants.DB_PORT;
import static com.databasecourse.erfan.Constants.DB_URL;

@Service
public class RunSQLService implements IRunSQLService{

    @Value("${spring.datasource.username}")
    private String rootUserName;

    @Value("${spring.datasource.password}")
    private String rootUserPassword;

    @Value("${spring.datasource.url}")
    private String dbURL;

    @Override
    public String cleanSQLString(String sql) {
        // Use regular expression to match multiple spaces and indentation
        // Replace them with a single space
        String cleanedSql = sql.replaceAll("\\s+", " ");

        return cleanedSql;
    }

    @Override
    public List<String> splitSQLQueries(String sql) {
        List<String> queries = new ArrayList<>();

        // Use regular expression to split the input SQL string by semicolons
        // The pattern "\\s*;\\s*" matches semicolons with optional surrounding spaces
        String[] queryArray = sql.split("\\s*;\\s*");

        // Add each split query to the list
        for (String query : queryArray) {
            queries.add(query.trim()); // Trim to remove leading/trailing spaces
        }

        return queries;
    }

    public int runQuery(String query){
        System.out.println("query: " + query);
        try (Connection con = DriverManager.getConnection(dbURL, rootUserName, rootUserPassword)){
            Statement st = con.createStatement();
            int rs = st.executeUpdate(query);
            System.out.println(rs);
            return rs;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return -1;
    }

    public String runQueryExec(String query){
        try (Connection con = DriverManager.getConnection(dbURL, rootUserName, rootUserPassword)){
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            String rss = SQLResultToString.resultSetToString(rs, false);
            rs.close();
            return rss;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }


    public SQLexecResults runQueryExecForGrading(String query){
        SQLexecResults sqLexecResults = new SQLexecResults();
        try (Connection con = DriverManager.getConnection(dbURL, rootUserName, rootUserPassword)){
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            String rss = SQLResultToString.resultSetToString(rs, false);
            rs.close();
            sqLexecResults.setResult(rss);
            sqLexecResults.setStatus(true);

        } catch (SQLException ex) {
            sqLexecResults.setSqlException(ex);
            sqLexecResults.setStatus(false);
            sqLexecResults.setError("Problem with SQL query" + ex.getMessage());
            System.out.println(ex.getMessage());
        }
        return sqLexecResults;
    }


    public ResultSet runQueryExecRS(String query){
        try (Connection con = DriverManager.getConnection(dbURL, rootUserName, rootUserPassword)){
            Statement st = con.createStatement();
            return st.executeQuery(query);
//            String rss = SQLResultToString.resultSetToString(rs, false);
//            rs.close();


        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public String runQueryExecThrowException(String query) throws SQLException {
            Connection con = DriverManager.getConnection(dbURL, rootUserName, rootUserPassword);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            String rss = SQLResultToString.resultSetToString(rs, false);
            rs.close();
            return rss;
    }

    public ShowUserDBDto getUserDBStat(String dbName){
        String userDbURL = "jdbc:mysql://"+DB_URL+":"+DB_PORT+"/" + dbName;

//        String showDBStatusQuery = "";
        try (Connection connection = DriverManager.getConnection(userDbURL, rootUserName, rootUserPassword)){

            DatabaseMetaData metaData = connection.getMetaData();

            ShowUserDBDto databaseDto = new ShowUserDBDto(dbName);

            // Get a list of all table names in the database.
            ResultSet tableResultSet = metaData.getTables(dbName, null, "%", new String[]{"TABLE"});

            // Loop through each table and describe its columns.
            while (tableResultSet.next()) {
                String tableName = tableResultSet.getString("TABLE_NAME");

                ShowUserDBTableDto tableDto = new ShowUserDBTableDto(tableName);

//                System.out.println("Table: " + tableName);

                // Get the columns for the current table.
                ResultSet columnResultSet = metaData.getColumns(dbName, null, tableName, null);
                while (columnResultSet.next()) {
                    String columnName = columnResultSet.getString("COLUMN_NAME");
                    String dataType = columnResultSet.getString("TYPE_NAME");
                    boolean isNullable = columnResultSet.getInt("NULLABLE") == DatabaseMetaData.columnNullable;
                    boolean isPrimaryKey = columnResultSet.getString("COLUMN_NAME").equalsIgnoreCase(tableName);

                    ShowUserDBTableColumnDto column = new ShowUserDBTableColumnDto(columnName, dataType, isNullable, isPrimaryKey);

                    tableDto.getColumns().add(column);


//                    System.out.println("\tColumn: " + columnName + ", Type: " + dataType +
//                            ", Nullable: " + isNullable + ", Primary Key: " + isPrimaryKey);
                }
                columnResultSet.close();
                databaseDto.getTables().add(tableDto);
            }


            tableResultSet.close();

            // Close the database connection.
            connection.close();
            return databaseDto;


        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public ResultSet runQueryExecWithUserDb(String query, String dbName){
        String customDBUrlForUsr = "jdbc:mysql://" + DB_URL+":"+DB_PORT +"/"+dbName;
        try (Connection con = DriverManager.getConnection(customDBUrlForUsr, rootUserName, rootUserPassword)){
            Statement st = con.createStatement();
            return st.executeQuery(query);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public String runQueryExecWithUserDbStringRet(String query, String dbName){
        String customDBUrlForUsr = "jdbc:mysql://" + DB_URL+":"+DB_PORT +"/"+dbName;
        try (Connection con = DriverManager.getConnection(customDBUrlForUsr, rootUserName, rootUserPassword)){
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            String rss = SQLResultToString.resultSetToString(rs, false);
            rs.close();
            return rss;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return ex.getMessage();
        }
    }

    public String runQueryExecWithUserDbStringRetThrowEx  (String query, String dbName) throws SQLException {
        String customDBUrlForUsr = "jdbc:mysql://" + DB_URL+":"+DB_PORT +"/"+dbName;
        Connection con = DriverManager.getConnection(customDBUrlForUsr, rootUserName, rootUserPassword);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            String rss = SQLResultToString.resultSetToString(rs, false);
            rs.close();
            return rss;

    }





    public int runQueryExecWithUserDbNoReturnStatement(String query, String dbName){

        System.out.println("Running SQL statement with no return statement: " + query);
        String customDBUrlForUsr = "jdbc:mysql://" + DB_URL+":"+DB_PORT +"/"+dbName;
        try (Connection con = DriverManager.getConnection(customDBUrlForUsr, rootUserName, rootUserPassword)){
            Statement st = con.createStatement();
            return st.executeUpdate(query);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return -1;
        }

    }


    public void backupDB  (String dbName, String backupPath){

        String command = "mysqldump -u " + rootUserName + " -p " + rootUserPassword +  " " + dbName + " -r " + backupPath;
//        String command = "mysqldump --user=" + rootUserName + " --password=" + rootUserPassword + " " + dbName + " > " + backupPath;
        System.out.println("Backup command: " + command);
        try {

            Runtime rt = Runtime.getRuntime();
            Process process = rt.exec(command);

            String input = rootUserPassword + "\n"; // Add '\n' to simulate pressing Enter
            process.getOutputStream().write(input.getBytes());
            process.getOutputStream().close();


//            BufferedReader stdInput = new BufferedReader(new
//                    InputStreamReader(process.getInputStream()));
//
//            BufferedReader stdError = new BufferedReader(new
//                    InputStreamReader(process.getErrorStream()));
//
//// Read the output from the command
//            System.out.println("Here is the standard output of the command:\n");
//            String s = null;
//            while ((s = stdInput.readLine()) != null) {
//                System.out.println(s);
//            }
//
//// Read any errors from the attempted command
//            System.out.println("Here is the standard error of the command (if any):\n");
//            while ((s = stdError.readLine()) != null) {
//                System.out.println(s);
//            }


//            int exitCode = process.waitFor();

//            if (exitCode == 0) {
//                System.out.println("Backup successful.");
//            } else {
//                System.err.println("Error during database dump.");
//            }

//            Process process = Runtime.getRuntime().exec(command);
//            int exitCode = process.waitFor();
//
//            if (exitCode == 0) {
//                System.out.println("Backup successful.");
//            } else {
//                System.out.println("Backup failed.");
//            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
    }


    public void restoreDB (String dbName, String restorePath) {

        String[] command = new String[]{"mysql", dbName, "--user=" + rootUserName, "--password=" + rootUserPassword, "-e", "source " + restorePath};

        try {
            Process process = new ProcessBuilder(command).start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Restoration successful.");
            } else {
                System.out.println("Restoration failed.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getTableNames(String dbName) {

        List<String> tableNames = new ArrayList<>();

        String customDbUrl = "jdbc:mysql://"+DB_URL+":"+DB_PORT+"/" + dbName;


        try (Connection connection = DriverManager.getConnection(customDbUrl, rootUserName, rootUserPassword)){

            String query = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA='"+dbName+"'";

            Statement st = connection.createStatement();
            ResultSet resultSet = st.executeQuery(query);


            while (resultSet.next()) {
                // Access data as needed
                String name = resultSet.getString("TABLE_NAME");
                tableNames.add(name);
            }
            resultSet.close();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }



        return tableNames;
    }


}
