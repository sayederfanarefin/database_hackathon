package com.databasecourse.erfan.service;


import com.databasecourse.erfan.web.dto.CustomDBTableDto;
import com.databasecourse.erfan.web.dto.CustomdbDto;
import com.databasecourse.erfan.web.dto.ShowUserDBDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICustomdbService {

    List<CustomDBTableDto> getCustomDbToDisplayThyme(Long id);

    String createCustomDB(CustomdbDto customdbDto);
    String updateCustomDB(CustomdbDto customdbDto);
    ShowUserDBDto getCustomDbToDisplaySchemaThyme(Long id);
    Page<CustomdbDto> getAllCustomDBs(int pageno);

    List<CustomdbDto> getAllCustomDBs();

    CustomdbDto getCustondbDto(Long id);
    void deleteCustomdb(Long customId);

    void resetDb(Long dbId);

    String runQuery(Long dbId, String queryToExectue);

    String runDBCreationQueries(String queryAll, String dbname, boolean dropDBIfFails);

    String createDBwithName(String dbName);
    void dropDB(String dbName);
}
