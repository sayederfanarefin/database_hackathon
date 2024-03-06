package com.databasecourse.erfan.web.controller;


import com.databasecourse.erfan.Constants;
import com.databasecourse.erfan.persistence.dao.UsertaskRepository;
import com.databasecourse.erfan.persistence.model.Hackathon;
import com.databasecourse.erfan.persistence.model.User;
import com.databasecourse.erfan.persistence.model.Usertask;
import com.databasecourse.erfan.service.CustomdbService;
import com.databasecourse.erfan.service.HackathonService;
import com.databasecourse.erfan.service.TaskService;
import com.databasecourse.erfan.web.dto.HackathonDtoNoTask;
import com.databasecourse.erfan.web.dto.TaskDto;
import com.databasecourse.erfan.web.util.CheckIfAdmin;
import com.databasecourse.erfan.web.util.PagerModel;
import com.databasecourse.erfan.web.util.StringTruncator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormatSymbols;
import java.time.LocalDateTime;


@Controller
@RequestMapping(value = {"/task"})
public class TaskController {
	private static final int BUTTONS_TO_SHOW = 9;
	@Autowired
	private TaskService taskService;

	@Autowired
	private UsertaskRepository userTasksRepository;

	@Autowired
	private HackathonService hackathonService;
	@Autowired
	private CustomdbService customdbService;

	public static String getMonthForInt(int m) {
		String month = "invalid";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		if (m >= 0 && m <= 11) {
			month = months[m];
		}
		return month;
	}

	@RequestMapping(value = {"/create"}, method = RequestMethod.GET)
	public Object create() {

		if (CheckIfAdmin.isAdmin()) {
			ModelAndView mv = new ModelAndView("createTaskAdmin");
			mv.addObject("pageTitle", "Create Task");
			mv.addObject("databasePlaceHolder", Constants.DATABASE_PLACEHOLDER);
			mv.addObject("uiCreateInstructions", Constants.TEXT_UI_CREATE_TEXT);

			mv.addObject("dbList", customdbService.getAllCustomDBs());
			mv.addObject("QUERY_TYPE_DML", Constants.QUERY_TYPE_DML);
			mv.addObject("QUERY_TYPE_DQL", Constants.QUERY_TYPE_DQL);



			return mv;
		} else {
			return null;
		}
	}

	@RequestMapping(value = {"/test"}, method = RequestMethod.GET)
	public Object test() {

		if (CheckIfAdmin.isAdmin()) {
			ModelAndView mv = new ModelAndView("createTaskTestQueryAdmin");
			mv.addObject("pageTitle", "Test Query Tool");
			mv.addObject("databasePlaceHolder", Constants.DATABASE_PLACEHOLDER);
			mv.addObject("uiCreateInstructions", Constants.TEXT_UI_TEST_QUERY);

			return mv;
		} else {
			return null;
		}
	}

	@RequestMapping(value = {"/editor/{itemid}"}, method = RequestMethod.GET)
	public ModelAndView viewCodeEditor(@PathVariable("itemid") Long itemid) {
		TaskDto task = taskService.getTask(itemid);
		Hackathon currentHackathon = hackathonService.getrOnGoingHackathon();


		User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ModelAndView mv;
		LocalDateTime currentTime = LocalDateTime.now();

		Usertask userTasks = userTasksRepository.findByUser_IdAndTask_IdAndHackathonId(authUser.getId(), task.getId(), currentHackathon.getId());

		if (userTasks.isComplete() || currentHackathon.getEndDateTime().isBefore(currentTime)){
			mv = new ModelAndView("redirect:/tasks");

		} else {

			mv = new ModelAndView("codeEditor");
			mv.addObject("currentHack", currentHackathon.getId());
			mv.addObject("currentHackEndTime", currentHackathon.getEndDateTime());
			mv.addObject("pageTitle", "Task Submission");
			mv.addObject("task", task);

			mv.addObject("user", authUser.getTeamName());

			if (task.isTestCasesCheck()){

				mv.addObject("customDbId", task.getCustomDbId());
				mv.addObject("customDbName", customdbService.getCustondbDto(task.getCustomDbId()).getName());
				mv.addObject("customDbDescription", customdbService.getCustondbDto(task.getCustomDbId()).getDescription());
			} else {
				mv.addObject("customDbDescription",null);
			}


		}
		return mv;
	}

	@RequestMapping(value = {"/view/{itemid}"}, method = RequestMethod.GET)
	public ModelAndView view(@PathVariable("itemid") Long itemid) {

		if (CheckIfAdmin.isAdmin()) {
			TaskDto task = taskService.getTask(itemid);
			ModelAndView mv = new ModelAndView("viewTaskAdmin");

			mv.addObject("pageTitle", "Task Details");
			mv.addObject("task", task);

//			System.out.println(task);
//			for (HackathonDtoNoTask hackathonDtoNoTask : task.getHackathons()){
//				System.out.println("hackathonDtoNoTask: " + hackathonDtoNoTask);
//			}
			if (task.isTestCasesCheck()){
				mv.addObject("customDb", customdbService.getCustondbDto(task.getCustomDbId()));
			}
			return mv;
		} else {
			return null;
		}
	}

	@RequestMapping(value = {"/edit/{itemid}"}, method = RequestMethod.GET)
	public ModelAndView edit(@PathVariable("itemid") Long itemid) {

		if (CheckIfAdmin.isAdmin()) {
			TaskDto task = taskService.getTask(itemid);
			ModelAndView mv = new ModelAndView("editTaskAdmin");

			mv.addObject("pageTitle", "Edit Task");
			mv.addObject("task", task);

			mv.addObject("databasePlaceHolder", Constants.DATABASE_PLACEHOLDER);
			mv.addObject("uiCreateInstructions", Constants.TEXT_UI_CREATE_TEXT);

			mv.addObject("dbList", customdbService.getAllCustomDBs());
			mv.addObject("QUERY_TYPE_DML", Constants.QUERY_TYPE_DML);
			mv.addObject("QUERY_TYPE_DQL", Constants.QUERY_TYPE_DQL);


			return mv;
		} else {
			return null;
		}
	}

	@RequestMapping(value = {"/view/all"}, method = RequestMethod.GET)
	public ModelAndView viewall(@RequestParam(value = "page", required = false, defaultValue = "0") int page) {
		if (CheckIfAdmin.isAdmin()) {
			ModelAndView modelandview = new ModelAndView("viewAllTasksAdmin");
			modelandview.addObject("pageTitle", "All Tasks");

			Page<TaskDto> taskList = taskService.getAllTasks(page);

			for (TaskDto taskDto : taskList) {
				taskDto.setDescription(StringTruncator.truncateString(taskDto.getDescription()));
			}

			modelandview.addObject("data", taskList);
			//System.out.println(employeeList.getTotalPages()+" "+employeeList.getTotalElements());
			PagerModel pager = new PagerModel(taskList.getTotalPages(), taskList.getNumber(), BUTTONS_TO_SHOW);
			modelandview.addObject("pager", pager);
			return modelandview;
		} else {
			return null;
		}

	}


	@RequestMapping(value = {"/view/all/{page}"}, method = RequestMethod.GET)
	public ModelAndView viewallByPage(@PathVariable(value = "page", required = true) int page) {
		if (CheckIfAdmin.isAdmin()) {
			ModelAndView modelandview = new ModelAndView("viewAllTasksAdmin");
			modelandview.addObject("pageTitle", "All Tasks");
			System.out.println("------- page --------: " + page);
			Page<TaskDto> taskList = taskService.getAllTasks(page);

			for (TaskDto taskDto : taskList) {
				taskDto.setDescription(StringTruncator.truncateString(taskDto.getDescription()));
			}

			modelandview.addObject("data", taskList);
			//System.out.println(employeeList.getTotalPages()+" "+employeeList.getTotalElements());
			PagerModel pager = new PagerModel(taskList.getTotalPages(), taskList.getNumber(), BUTTONS_TO_SHOW);
			modelandview.addObject("pager", pager);
			return modelandview;
		} else {
			return null;
		}

	}

}
