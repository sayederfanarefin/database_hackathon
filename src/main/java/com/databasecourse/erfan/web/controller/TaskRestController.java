package com.databasecourse.erfan.web.controller;

import com.databasecourse.erfan.persistence.model.Hackathon;
import com.databasecourse.erfan.persistence.model.Task;
import com.databasecourse.erfan.persistence.model.User;
import com.databasecourse.erfan.security.ISecurityUserService;
import com.databasecourse.erfan.service.*;
import com.databasecourse.erfan.web.dto.*;
import com.databasecourse.erfan.web.util.GenericResponse;
import com.sun.management.OperatingSystemMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.List;

@RestController
@RequestMapping(value = {"/task"})
public class TaskRestController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private ILeaderboardService leaderboardService;
    @Autowired
    private ITaskService taskService;

    @Autowired
    private CustomdbService customdbService;

    @Autowired
    private ISecurityUserService securityUserService;

    @Autowired
    private GradingService gradingService;


    @Autowired
    private IRunSQLService runSQLService;

    public TaskRestController() {
        super();
    }

    // Registration
    @PostMapping("/create")
    public GenericResponse createTask(@Valid final TaskDto taskDto) {
        LOGGER.debug("Create task with information: {}", taskDto);
        System.out.println("---------- task create -----------");
        System.out.println(taskDto.toString());
        taskService.createTask(taskDto);
        return new GenericResponse("success");
    }

    @PostMapping("/edit")
    public GenericResponse editTask(@Valid final TaskDto taskDto) {
        System.out.println("---------- task update -----------");

        System.out.println(taskDto.toString());
        taskService.updateTask(taskDto);
//        taskService.createTask(taskDto);
        return new GenericResponse("success");
    }

    @PostMapping("/delete/{itemid}/{deleteHacks}")
    public GenericResponse deleteTask(@PathVariable("itemid") Long itemid, @PathVariable("deleteHacks") boolean deleteHacks ) {
        taskService.deleteTaskWithHacks(itemid, deleteHacks);
        return new GenericResponse("success");
    }

    @PostMapping("/test")
    public TaskTestQueryDto testTask(@Valid final TaskTestQueryDto taskTestQueryDto) {
        LOGGER.debug("Create task with information: {}", taskTestQueryDto);
        System.out.println("---------- task query test -----------");
        TaskTestQueryDto temp = taskService.testQueryTask(taskTestQueryDto);
        System.out.println(temp.getTestQueryResults());
        return temp;
//        return new GenericResponse("success");
    }


    @PostMapping("/test/resetTempDB")
    public boolean resetTempDB(@RequestBody String requestData) {

        System.out.println("--- reset temp db ----" + requestData);
        return taskService.resetTempDB();

    }

    @PostMapping(value = {"/editor/submit"})
    public GenericResponse submissionFromCodeEditor(@RequestBody final CodeSubmitDto codeSubmitDto) {
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        GenericResponse gr;

        String results = gradingService.checkSubmission(authUser.getId(), codeSubmitDto.getTask(), codeSubmitDto.getSql(), codeSubmitDto.getHackathonId());
        System.out.println("Output inside controller of editor submit: "+ results);
        gr = new GenericResponse(results);
        return gr;
    }

    @PostMapping(value = {"/editor/submit/start"})
    public GenericResponse submissionFromCodeEditor(@RequestBody final StartDateSubmitDto startDateSubmitDto) {
        System.out.println("------ Start time rewuest submitted ------");
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String returnedText = gradingService.startTime(startDateSubmitDto.getHackathonId(), startDateSubmitDto.getTask(), authUser.getId());
        return new GenericResponse(returnedText);
    }


    @GetMapping("/database-status")
    @ResponseBody
    public ShowUserDBDto getDBStats() {

        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return runSQLService.getUserDBStat(authUser.getDatabaseName());
    }

    @GetMapping("/database-status/{itemid}")
    @ResponseBody
    public List<CustomDBTableDto> getCustomDBStats(@PathVariable("itemid") Long itemid) {
        return customdbService.getCustomDbToDisplayThyme(itemid);
    }

    @GetMapping("/database-status/schema/{itemid}")
    @ResponseBody
    public ShowUserDBDto getCustomDBSchema(@PathVariable("itemid") Long itemid) {
        System.out.println("----------getting custom db schema-------");
        return customdbService.getCustomDbToDisplaySchemaThyme(itemid);
    }

    @GetMapping("/task-status/{itemid}")
    @ResponseBody
    public TaskStatCodeEditorDto getTaskStats(@PathVariable("itemid") Long itemid) {

        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Hackathon currentHackathon = leaderboardService.getCurrentHackathon();

        TaskDto taskDto = taskService.getTask(itemid);

        TaskStatCodeEditorDto taskStatCodeEditorDto = new TaskStatCodeEditorDto();
        taskStatCodeEditorDto.setAverageAttemptsAllUsers(leaderboardService.averageAttemptsAllUser(currentHackathon.getId(), itemid));
        taskStatCodeEditorDto.setOverAllProgress(leaderboardService.completedUserTasksPercentage(authUser, currentHackathon));
        taskStatCodeEditorDto.setMaxAllowedAttempts(taskDto.getMaxAllowedAttempts());
        taskStatCodeEditorDto.setTaskStatus(taskService.isTaskComplete(authUser.getId(), currentHackathon.getId(), taskDto.getId()));
        Integer attemptsLeft =0;
        Integer attempts = taskService.totalAttemptsUsed(authUser.getId(), currentHackathon.getId(), taskDto.getId());
        if (attempts < taskStatCodeEditorDto.getMaxAllowedAttempts()) {
            attemptsLeft = taskStatCodeEditorDto.getMaxAllowedAttempts() - attempts;
        }
        taskStatCodeEditorDto.setAttemptsLeft(attemptsLeft);
        System.out.println("taskStatCodeEditorDto" + taskStatCodeEditorDto);
        return taskStatCodeEditorDto;
    }


}
