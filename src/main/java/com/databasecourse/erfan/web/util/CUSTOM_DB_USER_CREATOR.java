package com.databasecourse.erfan.web.util;

import com.databasecourse.erfan.persistence.model.User;
import com.databasecourse.erfan.service.RunSQLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CUSTOM_DB_USER_CREATOR {
    @Autowired
    private RunSQLService runSQLService;
    public void createDatabaseWithPermission(String password, User userx){
        String query = "CREATE DATABASE " + userx.getDatabaseName();
        runSQLService.runQuery(query);
        String query2_1 = "DROP USER '"+userx.getDatabaseUserName()+"'@'%';";
        runSQLService.runQuery(query2_1);
        String query2_2 = "FLUSH PRIVILEGES;";
        runSQLService.runQuery(query2_2);
        String query2 = "CREATE USER '"+userx.getDatabaseUserName()+"'@'%' IDENTIFIED BY '"+password+"';";
        runSQLService.runQuery(query2);
        String query3 = "GRANT ALL PRIVILEGES ON "+userx.getDatabaseName()+".* TO '"+userx.getDatabaseUserName()+"'@'%';";
        runSQLService.runQuery(query3);
        String query4 = "FLUSH PRIVILEGES;";
        runSQLService.runQuery(query4);
    }

    public void deleteUserDatabase(User userx){
        String query = "DROP DATABASE " + userx.getDatabaseName();
        runSQLService.runQuery(query);
    }
}
