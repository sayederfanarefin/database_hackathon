package com.databasecourse.erfan.persistence.dtoConverters;

import com.databasecourse.erfan.persistence.dao.CustomdbRepository;
import com.databasecourse.erfan.persistence.model.Task;
import com.databasecourse.erfan.persistence.model.Testcase;
import com.databasecourse.erfan.web.dto.TaskDto;
import com.databasecourse.erfan.web.dto.TestCaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskDtoModelConverrter {

    @Autowired
    private TestCasesModelConverter testCasesModelConverter;

    @Autowired
    private CustomdbRepository customdbRepository;

    private HackathonDtoModelConverrter hackathonDtoModelConverrter;

    public TaskDtoModelConverrter() {
        this.hackathonDtoModelConverrter = new HackathonDtoModelConverrter();
    }

    public TaskDto convert(Task task){
        TaskDto taskDto = new TaskDto();
        taskDto.setOutPutQueryCheck(task.isOutPutQueryCheck());

        taskDto.setDescription(task.getDescription());
        taskDto.setTitle(task.getTitle());
        taskDto.setId(task.getId());
        taskDto.setLogCheck(task.isLogCheck());
        taskDto.setFullQueryToSearchFor(task.getFullQueryToSearchFor());
        taskDto.setOutputTestQuery(task.getOutputTestQuery());
        taskDto.setOutputTestMatchString(task.getOutputTestMatchString());
        taskDto.setMaxAllowedAttempts(task.getMaxAllowedAttempts());
        taskDto.setQueryType(task.getQueryType());
        taskDto.setPrivateNotes(task.getPrivateNotes());
        taskDto.setFeedBack(task.getFeedBack());

        taskDto.setTestCasesCheck(task.isTestCasesCheck());
        if (task.isTestCasesCheck()){
            taskDto.setCustomDbId(task.getCustomdb().getId());
        }


        List<TestCaseDto> testCaseDtoList = new ArrayList<>();
        for ( Testcase testCase : task.getTestCases()){
            testCaseDtoList.add(testCasesModelConverter.convert(testCase));
        }
        taskDto.setTestCases(testCaseDtoList);
        taskDto.setHackathons(hackathonDtoModelConverrter.convertListNoTask(task.getHackathons().stream().collect(Collectors.toList())));

        return taskDto;

    }


    public Task convert(TaskDto taskDto){
        Task task = new Task();
        task.setOutPutQueryCheck(taskDto.isOutPutQueryCheck());
        task.setDescription(taskDto.getDescription());
        task.setTitle(taskDto.getTitle());
        task.setId(taskDto.getId());
        task.setLogCheck(taskDto.isLogCheck());
        task.setFullQueryToSearchFor(taskDto.getFullQueryToSearchFor());
        task.setOutputTestQuery(taskDto.getOutputTestQuery());
        task.setOutputTestMatchString(taskDto.getOutputTestMatchString());
        task.setMaxAllowedAttempts(taskDto.getMaxAllowedAttempts());
        task.setTestCasesCheck(taskDto.isTestCasesCheck());
        task.setQueryType(taskDto.getQueryType());
        task.setFeedBack(taskDto.getFeedBack());
        task.setPrivateNotes(taskDto.getPrivateNotes());

        if (task.isTestCasesCheck()){
            task.setCustomdb(customdbRepository.findById(taskDto.getCustomDbId()).get());

            List<Testcase> testCaseList = new ArrayList<>();
            for ( TestCaseDto testCaseDto : taskDto.getTestCases()){
                Testcase testCase = testCasesModelConverter.convert(testCaseDto);
                testCase.setTask(task);
                testCaseList.add(testCase);
            }
            task.setTestCases(testCaseList);
        }


        return task;

    }

    public List<TaskDto> convertList(List<Task> tasks){
        List<TaskDto> taskDtos = new ArrayList<>();
        for (Task task : tasks) {
            taskDtos.add(convert(task));
        }
        return taskDtos;
    }

    public List<Task> convertListTask(List<TaskDto> tasks){
        List<Task> taskDtos = new ArrayList<>();
        for (TaskDto task : tasks) {
            taskDtos.add(convert(task));
        }
        return taskDtos;
    }

}
