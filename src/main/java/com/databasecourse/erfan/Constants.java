package com.databasecourse.erfan;

public class Constants {
    public static final String  USER_ROLE = "ROLE_USER";
    public static final String  ADMIN_ROLE = "ROLE_ADMIN";

    public static final int PAGE_SIZE = 10;
    public static final int MAX_TEXT_CONTENT_LENGTH = 40;
    public static final int POINT_FOR_COMPLETING_TASK = 10;
    public static final double BONUS_SECOND_MULTIPLIER = 0.01;
    public static final int SCHEDULE_LEADERBOARD_CHECKER = 10000; // 10 sec, in milli seconds

    public static final String DATE_TIME_DISPLAY_PATTERN = "MMM dd, yyyy, HH:mm:ss";
    public static final String DATABASE_PLACEHOLDER = "$database--name$";
    public static final String USER_DB_NAME_PREFIX = "hack_";
    public static final String EMAIL_POSTFIX = "@ttu.edu";
    public static final String TEMP_DB = "temp_db";

    public static final String LOG_TABLE = "mysql.general_log";
    public static final String LOG_DB = "mysql";
    public static final String TEMP_DB_RESET_OPERATION_1 = "DROP DATABASE "+TEMP_DB+";";
    public static final String TEMP_DB_RESET_OPERATION_2 = "CREATE DATABASE "+TEMP_DB+";";
    public static final String TEMP_DB_CREATE = "DROP DATABASE "+TEMP_DB+";CREATE DATABASE "+TEMP_DB+";";

    public static final String TEXT_UI_TEST_QUERY = "In this window you can run test queries and it will show results based on a database named 'temp'. This database is periodically re-created every day. You can also re-create (DROP Temp Database and CREATE Temp Database) it using the 'Recreate DB' button. The resulting string is in the format of Java Result Sets. Which will eventually be used to compare hackathon participants performance ";
    public static final String TEXT_UI_CUSTOM_DB_TEST_QUERY = "In this window you can run test queries on the selected database in order to create test cases in the main creation windo for tasks. ";
    public static final String TEXT_UI_CREATE_TEXT = "You can create two types of tasks:\n" +
            "1. Custom Database Task: For this you can only use the test cases part, where you have to use at least one custom db. \n" +
            "2. User Database Task: Every user has its own empty database. You can assign tasks that modifies the user's database. Based on this you can do the following checks:\n" +
            "    i. Have one output query and figure out what the output query will produce. This will do a string matching of user's output and provided output after the output query is executed for the task. \n"+
            "    ii. You can directly check for SQL's using the log checking. This will do a string matching on the user's provided sql and your provided sql for the task. \n" +
            "    iii. You can use both i and ii. \n" +
            "****** You can do either 1 or 2 (Custom Database Task or User Database Task) ******\n"+
     "In your query for the option 2(i), if you have to use a database name in the output query, you can use the following placeholder in the query, which will later be replaced by each user corresponding user's database name: ";

    public static final String DB_URL="cs4354-fall2023.ttu.edu";
//    public static final String DB_URL="localhost";
    public static final String DB_PORT = "3306";

    public static final String UPLOAD_LOCATION_COMMON = "/upload-dir";
    public static final String UPLOAD_LOCATION_ADMIN = "/upload-dir-admin";


//    public static final String TEMP_SQL_BACKUP_LOCATION_COMMON = "/backup-temp";
//    public static final String TEMP_SQL_BACKUP_LOCATION = "src/main/webapp" + TEMP_SQL_BACKUP_LOCATION_COMMON;

    public static final int IMAGE_SAVE_HEIGHT_PX = 200;
    public static final int IMAGE_SAVE_WIDTH_PX = 200;

    public static final double POINTS_FACTOR_ATTEMPTS = 0.3; // 100.0
    public static final double POINTS_FACTOR_COMPLETE= 0.0; // 30.0
    public static final double POINTS_FACTOR_TIME= 0.7; // 0.1;
    public static final int RANDOM_PASSWORD_LENGTH= 10; // 0.1;


    public static final String USER_REGISTRATION_SUCESS_MSG= "User registered!";

    public static final String QUERY_TYPE_DML = "DML (Data Manipulation Language)";
    public static final String QUERY_TYPE_DQL = "DQL (Data Query Language)";
    public static final String DUMMY_PROFILE_PIC = "/app-assets/css/dummy.png";
    public static final String DB_CREATION_ERROR_TEXT = "Something went wrong creating the database. Most probably the database exists. Try a different name";
    public static final String CUSTOM_DB_TABLE_QUERY_ERROR_TEXT = "Something is wrong with your query. Please verify using the Custom DB Query Tool";

//    public static final String
}
