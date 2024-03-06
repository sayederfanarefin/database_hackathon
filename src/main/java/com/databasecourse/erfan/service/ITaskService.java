package com.databasecourse.erfan.service;


import com.databasecourse.erfan.persistence.model.Task;
import com.databasecourse.erfan.persistence.model.Usertask;
import com.databasecourse.erfan.web.dto.TaskDto;
import com.databasecourse.erfan.web.dto.TaskTestQueryDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ITaskService {

    Page<TaskDto> getAllTasks(int pageno);

    Task getTaskFull(Long id);
    boolean createTask(TaskDto taskDto);
    boolean updateTask(TaskDto taskDto);

    TaskDto getTask(Long id);
    TaskTestQueryDto testQueryTask(TaskTestQueryDto taskTestQueryDto);

    List<TaskDto> getAllTasks();

    boolean resetTempDB();

    List<Usertask> getAllTasksByUser(Long userId, Long hackathonId);

    void deleteTask(Long taskId);
    void deleteTask(Task task);
    void deleteTaskWithHacks(Long taskId, boolean deleteHacks);

    boolean isTaskComplete(Long userId, Long hackathonId, Long taskId);
    Integer totalAttemptsUsed(Long userId, Long hackathonId, Long taskId);
}
