package com.databasecourse.erfan.service;


import com.databasecourse.erfan.web.dto.TaskDto;
import com.databasecourse.erfan.web.dto.TaskTestQueryDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IReadMySQLLogServie {

    String readLogForUser(String mySqlUserName);

}
