package com.databasecourse.erfan.service;

import com.databasecourse.erfan.Constants;
import com.databasecourse.erfan.persistence.dao.HackathonRepository;
import com.databasecourse.erfan.persistence.dao.TaskRepository;
import com.databasecourse.erfan.persistence.dao.UsertaskRepository;
import com.databasecourse.erfan.persistence.dtoConverters.TaskDtoModelConverrter;
import com.databasecourse.erfan.persistence.model.Hackathon;
import com.databasecourse.erfan.persistence.model.Task;
import com.databasecourse.erfan.persistence.model.Usertask;
import com.databasecourse.erfan.web.dto.TaskDto;
import com.databasecourse.erfan.web.dto.TaskTestQueryDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.databasecourse.erfan.Constants.PAGE_SIZE;

@Service
@Transactional
public class TaskService implements ITaskService {

    @Autowired
    private UsertaskRepository userTasksRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private RunSQLService runSQLService;

    private final ModelMapper modelMapper;

    @Autowired
    private HackathonRepository hackathonRepository;

    @Autowired
    private HackathonService hackathonService;

    @Autowired
    private TaskDtoModelConverrter taskDtoModelConverrter;

    public TaskService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Override
    public Page<TaskDto> getAllTasks(int pageno) {
        PageRequest pageRequest = PageRequest.of(pageno, PAGE_SIZE);
        Page<Task> pageTask = taskRepository.findAll(pageRequest);
        return pageTask.map(task -> modelMapper.map(task, TaskDto.class));

    }

    @Override
    public boolean createTask(TaskDto taskDto) {
        Task task = taskDtoModelConverrter.convert(taskDto);
        taskRepository.save(task);
        return true;
    }

    @Override
    public boolean updateTask(TaskDto taskDto) {

        taskRepository.save(taskDtoModelConverrter.convert(taskDto));
        return true;
    }

    @Override
    public TaskDto getTask(Long id) {

        return taskDtoModelConverrter.convert(taskRepository.findById(id).get());

    }

    @Override
    public Task getTaskFull(Long id) {

        return taskRepository.findById(id).get();

    }

    @Override
    public TaskTestQueryDto testQueryTask(TaskTestQueryDto taskTestQueryDto) {
        String query = taskTestQueryDto.getTestQuery().replace(Constants.DATABASE_PLACEHOLDER, Constants.TEMP_DB);

        System.out.println("----------------------------");
        System.out.println("----------------------------");
        System.out.println("----------------------------");
        System.out.println("----------------------------");
        String result = "";
        try {
            result = runSQLService.runQueryExecThrowException(query);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("running query without return statement");
            int returnCode = runSQLService.runQuery(query);
            if (returnCode == 0){
                result = "Query returned 0.";
            } else {
                result = "Query returned non-0.";
            }

            result = result + "This query does not have a return statement";
        }
        taskTestQueryDto.setTestQueryResults(result);
        return taskTestQueryDto;
    }

    public boolean resetTempDB(){
        runSQLService.runQuery(Constants.TEMP_DB_RESET_OPERATION_1);
        runSQLService.runQuery(Constants.TEMP_DB_RESET_OPERATION_2);
        return false;
    }


    @Override
    public List<TaskDto> getAllTasks() {
        return taskDtoModelConverrter.convertList(taskRepository.findAll());
    }

    @Override
    public List<Usertask> getAllTasksByUser(Long userId, Long hackathonId) {
        System.out.println("Trying to do: getAllTasksByUser");

                List<Usertask> userTasksDto = new ArrayList();
                for (Task task: taskRepository.findAllByHackathons_Id(hackathonId)){
                    Usertask userTasks = userTasksRepository.findByUser_IdAndTask_IdAndHackathonId(userId, task.getId(), hackathonId);
                    userTasksDto.add(userTasks);
                }


                return userTasksDto;

    }

    @Override
    public void deleteTask(Long taskId){
        Task task = taskRepository.findById(taskId).get();
        deleteTask(task);
    }

    @Override
    public void deleteTaskWithHacks(Long taskId, boolean deleteHacks){
        Task task = taskRepository.findById(taskId).get();
        List<Hackathon> hackathonWithOnlyOneThisTask = new ArrayList<>();
        for (Hackathon hackathon: task.getHackathons()){
            if (hackathon.getTasks().size() == 1 && deleteHacks){
                hackathonWithOnlyOneThisTask.add(hackathon);
            }
        }
        deleteTask(task);

        for (Hackathon hackathon: hackathonWithOnlyOneThisTask){
            hackathonService.deleteHackathon(hackathon);
        }
    }

    @Override
    public void deleteTask(Task task){
        for (Hackathon hackathon: task.getHackathons()){
            hackathon.getTasks().remove(task);
            hackathonRepository.save(hackathon);
        }
        for (Usertask userTasks: userTasksRepository.findByTask_Id(task.getId())){
            userTasksRepository.delete(userTasks);
        }
        taskRepository.delete(task);
    }


    @Override
    public boolean isTaskComplete(Long userId, Long hackathonId, Long taskId){
        return userTasksRepository.findByUser_IdAndTask_IdAndHackathonId(userId, taskId, hackathonId).isComplete();
    }
    @Override
    public Integer totalAttemptsUsed(Long userId, Long hackathonId, Long taskId){
        return userTasksRepository.findByUser_IdAndTask_IdAndHackathonId(userId, taskId, hackathonId).getNumberOfAttempts();
    }

}
