package com.databasecourse.erfan.service;

import com.databasecourse.erfan.Constants;
import com.databasecourse.erfan.persistence.dao.CustomdbRepository;
import com.databasecourse.erfan.persistence.dtoConverters.CustomdbDtoModelConverrter;
import com.databasecourse.erfan.persistence.model.Customdb;
import com.databasecourse.erfan.persistence.model.Task;
import com.databasecourse.erfan.web.dto.CustomDBTableDto;
import com.databasecourse.erfan.web.dto.CustomdbDto;
import com.databasecourse.erfan.web.dto.ShowUserDBDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.databasecourse.erfan.Constants.*;

@Service
@Transactional
@CacheConfig(cacheNames = "showUserDBDto") // Specify the cache name
public class CustomdbService implements ICustomdbService {

    @Autowired
    private CustomdbRepository customdbRepository;

    @Autowired
    private CustomdbDtoModelConverrter customdbDtoModelConverrter;

    @Autowired
    private RunSQLService runSQLService;

    @Autowired
    private TaskService taskService;

    private final ModelMapper modelMapper;

    public CustomdbService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CustomDBTableDto> getCustomDbToDisplayThyme(Long id) {
       Customdb customdb = customdbRepository.findById(id).get();

       List<CustomDBTableDto> tablesInfo = new ArrayList<>();
        List<String> tableNames  = runSQLService.getTableNames(customdb.getName());
        System.out.println("Table names");
        for ( String tableName: tableNames){
            System.out.println(tableName);
        }

       for ( String tableName: tableNames){
           try {
               tablesInfo.add(getTableData(tableName, customdb.getName()));
           } catch (SQLException ex){
                System.out.println("SQL Exception while getting table information of some customdb: " + ex.getMessage());
           } catch (Exception e){
               System.out.println("getCustomDbToDisplayThyme: " + e.getMessage());
           }
       }
       return tablesInfo;

    }

    @Cacheable("showUserDBDto")
    @Override
    public ShowUserDBDto getCustomDbToDisplaySchemaThyme(Long id) {
        Customdb customdb = customdbRepository.findById(id).get();

        ShowUserDBDto showUserDBDto = runSQLService.getUserDBStat(customdb.getName());

        return showUserDBDto;

    }

    @Value("${spring.datasource.username}")
    private String rootUserName;

    @Value("${spring.datasource.password}")
    private String rootUserPassword;



    private CustomDBTableDto getTableData(String tableName, String dbName) throws SQLException {
        CustomDBTableDto customDBTableDto = new CustomDBTableDto();

        List<Map<String, Object>> tableData = new ArrayList<>();

        customDBTableDto.setTableName(tableName);

        String sql;
//        if (tableName.equals("Sales")){
//            sql = "SELECT *, FROM_UNIXTIME(sale_date / 1000, '%Y-%m-%d') AS formatted_sale_date FROM + tableName";
//
//        } else {
            sql = "SELECT * FROM " + tableName;
//        }

//        runSQLService.runQueryExecWithUserDb(sql, dbName);
        String customDBUrlForUsr = "jdbc:mysql://" + DB_URL+":"+DB_PORT +"/"+dbName;
        Connection con = DriverManager.getConnection(customDBUrlForUsr, rootUserName, rootUserPassword);

        Statement st = con.createStatement();
        ResultSet resultSet =  st.executeQuery(sql);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (resultSet.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {

                String columnName = metaData.getColumnName(i);
                Object columnValue = resultSet.getObject(i);
//                if (columnName.equals("sale_date") && tableName.equals("Sales")){
//
//                }
                row.put(columnName, columnValue);
            }
            tableData.add(row);
        }
        resultSet.close();

        customDBTableDto.setTableData(tableData);

        return customDBTableDto;
    }



    @Override
    public String createCustomDB(CustomdbDto customdbDto) {
        String returnedTextDBCreation = createDBwithName(customdbDto.getName());
        if (!returnedTextDBCreation.equals(Constants.DB_CREATION_ERROR_TEXT)){
            String returnedText = runDBCreationQueries(customdbDto.getQuery(), customdbDto.getName(), true);
            if (!returnedText.contains(Constants.CUSTOM_DB_TABLE_QUERY_ERROR_TEXT)){
                customdbRepository.save(customdbDtoModelConverrter.convert(customdbDto));
            }
            return returnedText;
        } else {
            return returnedTextDBCreation;
        }
    }

    @Override
    public String createDBwithName(String dbName ) {

        String createDB = "CREATE DATABASE " + dbName +";";
        int returned  = runSQLService.runQuery(createDB);
        if (returned == -1){
            return Constants.DB_CREATION_ERROR_TEXT;
        }else {
            return "Database created!";
        }

    }

    @Override
    public String updateCustomDB(CustomdbDto customdbDto) {
        Customdb backup = customdbRepository.findById(customdbDto.getId()).get();

        dropDB(customdbDto.getName());

        String returnedTextDBCreation = createDBwithName(customdbDto.getName());
        if (!returnedTextDBCreation.equals(Constants.DB_CREATION_ERROR_TEXT)){
            String returnedText = runDBCreationQueries(customdbDto.getQuery(), customdbDto.getName(), true);
            if (!returnedText.contains(Constants.CUSTOM_DB_TABLE_QUERY_ERROR_TEXT)){
                customdbRepository.save(customdbDtoModelConverrter.convert(customdbDto));
            } else {
                // something wrong with the update, so we need to restore the previous db
                createDBwithName(backup.getName());
                runDBCreationQueries(backup.getQuery(), backup.getName(), false);
            }
            return returnedText;
        } else {
            return returnedTextDBCreation;
        }
    }

    @Override
    public Page<CustomdbDto> getAllCustomDBs(int pageno) {
        PageRequest pageRequest = PageRequest.of(pageno, PAGE_SIZE);
        Page<Customdb> customdbs = customdbRepository.findAll(pageRequest);
        return customdbs.map(task -> modelMapper.map(task, CustomdbDto.class));
    }

    @Override
    public List<CustomdbDto> getAllCustomDBs() {
        return customdbDtoModelConverrter.convertList(customdbRepository.findAll());
    }

    @Override
    public CustomdbDto getCustondbDto(Long id) {

        return customdbDtoModelConverrter.convert(customdbRepository.findById(id).get());
    }

    @Override
    public void deleteCustomdb(Long customId) {
        Customdb customdb = customdbRepository.findById(customId).get();

        for (Task task: customdb.getTasks()){
            taskService.deleteTask(task);
        }
        System.out.println("Deleting customdb because of customdb removal");
        customdbRepository.deleteById(customId);
        dropDB(customdb.getName());
    }


    public String runQuery(Long dbId, String queryToExectue){
        Customdb customDb = customdbRepository.findById(dbId).get();
        try {
            return runSQLService.runQueryExecWithUserDbStringRetThrowEx(queryToExectue, customDb.getName());
        } catch (SQLException e) {

            int returned = runSQLService.runQueryExecWithUserDbNoReturnStatement(queryToExectue, customDb.getName());

            if (returned == -1){
                return "There is some problem with the SQL statement.";
            } else {
                return "Executed";
            }

        }catch (Exception e) {
            return  e.getMessage();
        }
    }

    public void resetDb(Long dbId){
        Customdb customDb = customdbRepository.findById(dbId).get();
        dropDB(customDb.getName());
//        String createDB = "CREATE DATABASE " + customDb.getName() +";";
//        runSQLService.runQuery(createDB);
        createDBwithName(customDb.getName());
        runDBCreationQueries(customDb.getQuery(), customDb.getName(),false);
    }

    @Override
    public void dropDB(String dbName){
        String dropDbQuery = "DROP DATABASE " + dbName +";";
        runSQLService.runQuery(dropDbQuery);
    }


    @Override
    public String runDBCreationQueries(String queryAll, String dbname, boolean dropDBIfFails){
        boolean failed = false;

        for (String query : runSQLService.splitSQLQueries(queryAll)){
            int returnedStat = runSQLService.runQueryExecWithUserDbNoReturnStatement(
                    runSQLService.cleanSQLString( query),
                    dbname
            );
            System.out.println("runSQLService.runQueryExecWithUserDbNoReturnStatement" + returnedStat);

            if (returnedStat == -1 ){
                failed = true;
                break;
            }
        }

        if (failed){
            if (dropDBIfFails){
                dropDB(dbname);
            }

            return Constants.CUSTOM_DB_TABLE_QUERY_ERROR_TEXT;
        }

        return "Successful!";

    }



}
