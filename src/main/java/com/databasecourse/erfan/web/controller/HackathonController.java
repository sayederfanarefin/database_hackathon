package com.databasecourse.erfan.web.controller;


import com.databasecourse.erfan.service.HackathonService;
import com.databasecourse.erfan.service.LeaderboardService;
import com.databasecourse.erfan.service.TaskService;
import com.databasecourse.erfan.service.UserService;
import com.databasecourse.erfan.web.dto.HackathonDto;
import com.databasecourse.erfan.web.dto.HackathonDtoDisplay;
import com.databasecourse.erfan.web.dto.TaskDto;
import com.databasecourse.erfan.web.util.CheckIfAdmin;
import com.databasecourse.erfan.web.util.PagerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormatSymbols;


@Controller
@RequestMapping(value={ "/hackathon"})
public class HackathonController {

	private static final int BUTTONS_TO_SHOW = 9;

	@Autowired
	private LeaderboardService leaderboardService;

	@Autowired
    private TaskService taskService;

	@Autowired
	private HackathonService hackathonService;
    @Autowired
    private UserService userService;

    @RequestMapping(value={ "/create"},method = RequestMethod.GET)
    public Object create() {

		if (CheckIfAdmin.isAdmin()){
			ModelAndView mv = new ModelAndView("createHackathonAdmin");

			mv.addObject("pageTitle", "Create Hackathon");
			mv.addObject("tasks", taskService.getAllTasks());
			mv.addObject("users", userService.getAllUsers());
			return mv;
		} else {
			return null;
		}
    }

//	@RequestMapping(value={ "/edit"},method = RequestMethod.GET)
//	public Object edit() {
//
//		if (CheckIfAdmin.isAdmin()){
//			ModelAndView mv = new ModelAndView("editHackathonAdmin");
//
//			mv.addObject("pageTitle", "Update Hackathon");
//			mv.addObject("tasks", taskService.getAllTasks());
//			mv.addObject("users", userService.getAllUsers());
//			return mv;
//		} else {
//			return null;
//		}
//	}



	@RequestMapping(value={ "/edit/{itemid}"},method = RequestMethod.GET)
	public ModelAndView edit(@PathVariable("itemid") Long itemid) {

		if (CheckIfAdmin.isAdmin()){
			HackathonDtoDisplay hackDto = hackathonService.getHackathon(itemid);
			ModelAndView mv = new ModelAndView("editHackathonAdmin");

			mv.addObject("pageTitle", "Update Hackathon");
			mv.addObject("hackathon", hackDto);
			mv.addObject("availableTasks", taskService.getAllTasks());
			return mv;
		} else {
			return null;
		}
	}


//	@RequestMapping(value={ "/view"},method = RequestMethod.GET)
//	public ModelAndView view() {
//
//		if (CheckIfAdmin.isAdmin()){
//			ModelAndView mv = new ModelAndView("adminDashboard");
//			mv.addObject("pageTitle", "Hackathon");
//			return mv;
//		} else {
//			return null;
//		}
//	}

	@RequestMapping(value = {"/view/{itemid}"}, method = RequestMethod.GET)
	public ModelAndView view(@PathVariable("itemid") Long itemid) {

		if (CheckIfAdmin.isAdmin()) {
			HackathonDtoDisplay hackathonDisplay = hackathonService.getHackathon(itemid);


			ModelAndView mv = new ModelAndView("viewHackathonAdmin");

			mv.addObject("pageTitle", "Hackathon Details");
			mv.addObject("hackathon", hackathonDisplay);
			mv.addObject("gradeCenterItems", hackathonService.getGradeCenterItems(hackathonDisplay.getId()));
			return mv;
		} else {
			return null;
		}
	}


	@RequestMapping(value = {"/view/leaderboard/{itemid}"}, method = RequestMethod.GET)
	public ModelAndView viewLeeder(@PathVariable("itemid") Long itemid) {

		if (CheckIfAdmin.isAdmin()) {
			HackathonDtoDisplay hackathonDisplay = hackathonService.getHackathon(itemid);

			ModelAndView mv = new ModelAndView("leaderBoardIndividualAdmin");
			mv.addObject("data", leaderboardService.getHackathonLeaderboard(itemid));

			mv.addObject("pageTitle", "Hackathon Details");
			mv.addObject("hackathon", hackathonDisplay);

			return mv;
		} else {
			return null;
		}
	}

	@RequestMapping(value = {"/viewreport/{itemid}"}, method = RequestMethod.GET)
	public ModelAndView viewReport(@PathVariable("itemid") Long itemid) {

		if (CheckIfAdmin.isAdmin()) {
			HackathonDtoDisplay hackathonDisplay = hackathonService.getHackathon(itemid);


			ModelAndView mv = new ModelAndView("viewHackathonAdmin");
//			mv.addObject("pageTitle", "Hackathon Details");
//			mv.addObject("hackathon", hackathonDisplay);
//			mv.addObject("gradeCenterItems", hackathonService.getGradeCenterItems(hackathonDisplay.getId()));
			return mv;
		} else {
			return null;
		}
	}


	@RequestMapping(value={ "/view/all"},method = RequestMethod.GET)
	public ModelAndView viewall(@RequestParam(value = "page", required = false, defaultValue = "0")int page) {
		if (CheckIfAdmin.isAdmin()) {
			ModelAndView mv = new ModelAndView("viewAllHackathonAdmin");
			mv.addObject("pageTitle", "All Hackathons");

			Page<HackathonDtoDisplay> hackathonPage = hackathonService.getAllHackathons(page);
			mv.addObject("data", hackathonPage);
			PagerModel pager = new PagerModel(hackathonPage.getTotalPages(), hackathonPage.getNumber(), BUTTONS_TO_SHOW);
			mv.addObject("pager", pager);

			return mv;
		}else{
			return null;
		}

	}


    
    
    public static String getMonthForInt(int m) {
		String month = "invalid";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		if (m >= 0 && m <= 11) {
			month = months[m];
		}
		return month;
	}

 
}
