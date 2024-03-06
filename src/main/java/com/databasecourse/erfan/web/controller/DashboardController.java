package com.databasecourse.erfan.web.controller;


import com.databasecourse.erfan.persistence.dtoConverters.HackathonDtoModelConverrter;
import com.databasecourse.erfan.persistence.dtoConverters.LeaderElementDtoModelConverrter;
import com.databasecourse.erfan.persistence.dtoConverters.UserTasksModelConverter;
import com.databasecourse.erfan.persistence.model.Hackathon;
import com.databasecourse.erfan.persistence.model.User;
import com.databasecourse.erfan.persistence.model.Usertask;
import com.databasecourse.erfan.service.HackathonService;
import com.databasecourse.erfan.service.LeaderboardService;
import com.databasecourse.erfan.service.TaskService;
import com.databasecourse.erfan.service.UserService;
import com.databasecourse.erfan.web.dto.*;
import com.databasecourse.erfan.web.util.CheckIfAdmin;
import com.databasecourse.erfan.web.util.TimeStringMinuteFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.databasecourse.erfan.Constants.*;


@Controller
@RestController
public class DashboardController {

   @Autowired
   private LeaderboardService leaderboardService;

   @Autowired
   private LeaderElementDtoModelConverrter leaderElementDtoModelConverrter;
   @Autowired
   private HackathonService hackathonService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserTasksModelConverter userTasksModelConverter;

	@Autowired
	private HackathonDtoModelConverrter hackathonDtoModelConverrter;

    @RequestMapping(value={ "/home"},method = RequestMethod.GET)
    public Object home() {

		if (CheckIfAdmin.isAdmin()){
			return new RedirectView("/admin");
		} else {

			User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			ModelAndView mv = new ModelAndView("instructions");
			mv.addObject("pageTitle", "Instructions");
			mv.addObject("userDB", authUser.getDatabaseName());
			mv.addObject("userDBUser", authUser.getDatabaseUserName());
			mv.addObject("dbUrl", DB_URL);
			mv.addObject("dbPort", DB_PORT);
			mv.addObject("dashData", userDashContent(authUser));
			mv.addObject("previousHackathons", hackathonService.showPreviousHackathons(authUser));



			return mv;
		}

    }

	@GetMapping("/user/dash/data")
	public UserDashDto getUserDashData() {
		User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userDashContent(authUser);
	}

	@RequestMapping(value={ "/admin"},method = RequestMethod.GET)
	public ModelAndView homeAdmin() {

		if (CheckIfAdmin.isAdmin()){

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd (HH:mm)");

			ModelAndView mv = new ModelAndView("adminDashboard");
			mv.addObject("pageTitle", "Dashboard");

			boolean hackOnGoing = hackathonService.checkForOnGoingHackathon();
			mv.addObject("oneGoingHackathon", hackOnGoing);

			if (hackOnGoing){
				Hackathon currentHackathon = hackathonService.getrOnGoingHackathon();
				mv.addObject("currentHackathon", currentHackathon);

				mv.addObject("oneGoingHackathonEndTime", currentHackathon.getEndDateTime().format(formatter));

			}

			// Format LocalDateTime to a string
			Hackathon neatestHackathon = hackathonService.checkForNearestHackathon();

			if (neatestHackathon != null){
				String formattedDateTimeNeatestHackathon = neatestHackathon.getStartDateTime().format(formatter);
				mv.addObject("nearestHackathonStart", formattedDateTimeNeatestHackathon);
				mv.addObject("nearestHackathon", neatestHackathon);
			} else {
				mv.addObject("nearestHackathonStart", "No hackathon");
			}
			mv.addObject("numberOfUsers", userService.numberOfUsers());
			return mv;
		} else {
			return null;
		}
	}



	@GetMapping("/leaderboard/rest")
	public LeaderBoardRestResponse leaderBoardGet() {
		LeaderBoardRestResponse leaderBoardRestResponse = new LeaderBoardRestResponse();
		User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (!CheckIfAdmin.isAdmin()) {

			leaderBoardRestResponse.setActiveUserId(authUser.getId());
		} else {
			Long l = (long) -1;
			leaderBoardRestResponse.setActiveUserId(l);
		}
		leaderBoardRestResponse.setData(leaderElementDtoModelConverrter.convertListElements(leaderboardService.getCurrentLeaderboard()));
		Hackathon currentHackathon = leaderboardService.getCurrentHackathon();
		if (currentHackathon != null){
			LocalDateTime endDate = currentHackathon.getEndDateTime();
			leaderBoardRestResponse.setTimLeftYear(endDate.getYear());
			leaderBoardRestResponse.setTimLeftMonth(endDate.getMonthValue());
			leaderBoardRestResponse.setTimLeftDay(endDate.getDayOfMonth());
			leaderBoardRestResponse.setTimLeftHour(endDate.getHour());
			leaderBoardRestResponse.setTimLeftMin(endDate.getMinute());
			leaderBoardRestResponse.setTimLeftSec(endDate.getSecond());
		}
		return leaderBoardRestResponse;
	}

	@RequestMapping(value={ "/leaderboard"},method = RequestMethod.GET)
	public ModelAndView leaderBoard() {
		User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		ModelAndView mv = null;

		if (CheckIfAdmin.isAdmin()) {
			 mv = new ModelAndView("leaderboardAdmin");

		}else{
			mv = new ModelAndView("leaderboardUser");
			mv.addObject("activeUserId", authUser.getId());
		}

		Hackathon currentHackathon = leaderboardService.getCurrentHackathon();

		mv.addObject("data", leaderboardService.getCurrentLeaderboard());

		if (currentHackathon != null){
			LocalDateTime endDate = currentHackathon.getEndDateTime();

			mv.addObject("timLeftYear", endDate.getYear());
			mv.addObject("hackOnGoing", true);
			mv.addObject("timLeftMonth", endDate.getMonthValue());
			mv.addObject("timLeftDay", endDate.getDayOfMonth());
			mv.addObject("timLeftHour", endDate.getHour());
			mv.addObject("timLeftMin", endDate.getMinute());
			mv.addObject("timLeftSec", endDate.getSecond());
		} else {
			if (authUser != null){
				Hackathon upcoming;

				if (CheckIfAdmin.isAdmin()) {
					 upcoming = hackathonService.checkForNearestHackathon();
				} else {
					 upcoming = hackathonService.checkForNearestHackathon(authUser.getId());
				}

				if (upcoming != null){
					mv.addObject("upcomingHackathon", hackathonDtoModelConverrter.convert(upcoming));
				} else {
					mv.addObject("upcomingHackathon", null);
				}

			}
			mv.addObject("hackOnGoing", false);

		}
		mv.addObject("pageTitle", "Leader Board");
		return mv;
	}


	@RequestMapping(value={ "/generalboard"},method = RequestMethod.GET)
	public ModelAndView generalBoard() {
		User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		ModelAndView mv = null;
		Hackathon currentHackathon = leaderboardService.getCurrentHackathon();

		if (CheckIfAdmin.isAdmin()) {
			mv = new ModelAndView("generalboardAdmin");
			mv.addObject("userPosition", -1);
			mv.addObject("userScore", -1);
		}else{
			List<UserDashScoreBreakDownDto> usertasksBreakdowns = leaderboardService.getUserDashScoreBreakDown(authUser, currentHackathon);
			double totalPoints = 0.0;
			for (UserDashScoreBreakDownDto userDashScoreBreakDownDto: usertasksBreakdowns){
				if (userDashScoreBreakDownDto.getPoints() != null){
					totalPoints = totalPoints + userDashScoreBreakDownDto.getPoints();
				}
			}

			mv = new ModelAndView("generalboardUser");
			mv.addObject("activeUserId", authUser.getId());
			mv.addObject("userPosition", leaderboardService.getUserPosition(authUser));
			mv.addObject("userScore", totalPoints);
		}


		if (currentHackathon != null){
			LocalDateTime endDate = currentHackathon.getEndDateTime();

			mv.addObject("timLeftYear", endDate.getYear());
			mv.addObject("hackOnGoing", true);
			mv.addObject("timLeftMonth", endDate.getMonthValue());
			mv.addObject("timLeftDay", endDate.getDayOfMonth());
			mv.addObject("timLeftHour", endDate.getHour());
			mv.addObject("timLeftMin", endDate.getMinute());
			mv.addObject("timLeftSec", endDate.getSecond());
		} else {
			if (authUser != null){
				Hackathon upcoming;

				if (CheckIfAdmin.isAdmin()) {
					upcoming = hackathonService.checkForNearestHackathon();
				} else {
					upcoming = hackathonService.checkForNearestHackathon(authUser.getId());
				}

				if (upcoming != null){
					mv.addObject("upcomingHackathon", hackathonDtoModelConverrter.convert(upcoming));
				} else {
					mv.addObject("upcomingHackathon", null);
				}

			}
			mv.addObject("hackOnGoing", false);

		}
		mv.addObject("pageTitle", "General Board");


		List<String> teamNames = currentHackathon.getUsers().stream()
				.map(User::getTeamName)
				.collect(Collectors.toList());
		mv.addObject("teams",teamNames);

		System.out.println(teamNames);

		mv.addObject("averageScore",cachedGeneralBoard(currentHackathon).averageScore);
		mv.addObject("bestScore",cachedGeneralBoard(currentHackathon).bestScore);
		mv.addObject("averageAttemptsPerTask",cachedGeneralBoard(currentHackathon).averageAttemptsPerTask);
		mv.addObject("averageTimePerTask",cachedGeneralBoard(currentHackathon).averageTimePerTask);

		return mv;
	}

	@GetMapping("/generalboard/rest")
	public GeneralBoardRestResponse generalBoardGet() {
		GeneralBoardRestResponse generalBoardRestResponse = new GeneralBoardRestResponse();
		User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Hackathon currentHackathon = leaderboardService.getCurrentHackathon();

		if (!CheckIfAdmin.isAdmin()) {

			generalBoardRestResponse.setActiveUserId(authUser.getId());
			generalBoardRestResponse.setUserPosition(leaderboardService.getUserPosition(authUser));

			List<UserDashScoreBreakDownDto> usertasksBreakdowns = leaderboardService.getUserDashScoreBreakDown(authUser, currentHackathon);
			double totalPoints = 0.0;
			for (UserDashScoreBreakDownDto userDashScoreBreakDownDto: usertasksBreakdowns){
				if (userDashScoreBreakDownDto.getPoints() != null){
					totalPoints = totalPoints + userDashScoreBreakDownDto.getPoints();
				}
			}
			generalBoardRestResponse.setUserScore(totalPoints);


		} else {
			Long l = (long) -1;
			Integer i = (int) -1;
			generalBoardRestResponse.setActiveUserId(l);
			generalBoardRestResponse.setUserScore(l);
			generalBoardRestResponse.setUserPosition(i);

		}

		List<String> teamNames = currentHackathon.getUsers().stream()
				.map(User::getTeamName)
				.collect(Collectors.toList());
		generalBoardRestResponse.setTeams(teamNames);
		generalBoardRestResponse.setAverageScore(cachedGeneralBoard(currentHackathon).averageScore);
		generalBoardRestResponse.setBestScore(cachedGeneralBoard(currentHackathon).bestScore);
		generalBoardRestResponse.setAverageAttemptsPerTask(cachedGeneralBoard(currentHackathon).averageAttemptsPerTask);
		generalBoardRestResponse.setAverageTimePerTask(cachedGeneralBoard(currentHackathon).averageTimePerTask);



		if (currentHackathon != null){
			LocalDateTime endDate = currentHackathon.getEndDateTime();
			generalBoardRestResponse.setTimLeftYear(endDate.getYear());
			generalBoardRestResponse.setTimLeftMonth(endDate.getMonthValue());
			generalBoardRestResponse.setTimLeftDay(endDate.getDayOfMonth());
			generalBoardRestResponse.setTimLeftHour(endDate.getHour());
			generalBoardRestResponse.setTimLeftMin(endDate.getMinute());
			generalBoardRestResponse.setTimLeftSec(endDate.getSecond());
		}
		return generalBoardRestResponse;
	}

	@RequestMapping(value={ "/tasks"},method = RequestMethod.GET)
	public ModelAndView tasks() {
		if (!CheckIfAdmin.isAdmin()) {
			ModelAndView mv = new ModelAndView("tasks");
			mv.addObject("pageTitle", "Hackathon Tasks");
			User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			Hackathon currentHackathon = leaderboardService.getCurrentHackathon();
			List<UsertaskDto> tasks;

			if (currentHackathon == null){
				tasks = null;
				Hackathon hackathon = hackathonService.checkForNearestHackathon(authUser.getId());
				if (hackathon != null){
					mv.addObject("upcomingHackathon", hackathonDtoModelConverrter.convert(hackathon));
				}
			} else {
				tasks = userTasksModelConverter.convertListTruncated(taskService.getAllTasksByUser(authUser.getId(), currentHackathon.getId()));

				mv.addObject("hackathonTitle", currentHackathon.getTitle());
				mv.addObject("hackathonDescription", currentHackathon.getDescription());
				mv.addObject("hackathonStartTime", currentHackathon.getStartDateTime());
				mv.addObject("hackathonGeneralBoardEnabled", currentHackathon.isGeneralBoardEnabled());
				mv.addObject("hackathonLeaderBoardEnabled", currentHackathon.isLeaderBoardEnabled());

			}

			mv.addObject("tasks", tasks);

			return mv;
		}else{
			return null;
		}
	}

	@RequestMapping(value={ "/404"},method = RequestMethod.GET)
	public ModelAndView four04() {
		ModelAndView mv = new ModelAndView("404");
		return mv;
	}

	@RequestMapping(value={ "/500"},method = RequestMethod.GET)
	public ModelAndView five00() {
		ModelAndView mv = new ModelAndView("500");
		return mv;
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

	private Long calculateTimeLeftSecond(LocalDateTime end){
		LocalDateTime now = LocalDateTime.now();
		Duration remainingDuration = Duration.between(now, end);
		return remainingDuration.getSeconds();
	}

	private Double calculateTimeLeftPercentage(LocalDateTime start, LocalDateTime end){

		LocalDateTime now = LocalDateTime.now();
		Duration totalDuration = Duration.between(start, end);
		Duration remainingDuration = Duration.between(now, end);
		double percentageLeft = ((double) remainingDuration.toMillis() / totalDuration.toMillis()) * 100;
		DecimalFormat df = new DecimalFormat("#.00");
		String formatted = df.format(percentageLeft);
		return Double.parseDouble(formatted);
	}

	private UserDashDto userDashContent(User user){
		UserDashDto userDashDto = new UserDashDto();
		Hackathon currentHackathon = leaderboardService.getCurrentHackathon();


		if (currentHackathon != null) {
			userDashDto.setTimeLeft(calculateTimeLeftPercentage(currentHackathon.getStartDateTime(), currentHackathon.getEndDateTime()));
			userDashDto.setTimeLeftSec(calculateTimeLeftSecond( currentHackathon.getEndDateTime()));
			userDashDto.setPosition(leaderboardService.getUserPosition(user));
			userDashDto.setSolvedTasks(leaderboardService.completedUserTasks(user, currentHackathon));
			userDashDto.setSolvedTasksPercentage(leaderboardService.completedUserTasksPercentage(user, currentHackathon));
			List<UserDashScoreBreakDownDto> usertasksBreakdowns = leaderboardService.getUserDashScoreBreakDown(user, currentHackathon);

			userDashDto.setBreakDownItems(usertasksBreakdowns);
			double totalPoints = 0.0;
			for (UserDashScoreBreakDownDto userDashScoreBreakDownDto: usertasksBreakdowns){
				if (userDashScoreBreakDownDto.getPoints() != null){
					totalPoints = totalPoints + userDashScoreBreakDownDto.getPoints();
				}

			}
			userDashDto.setTotalPoints(totalPoints);
		}
		return userDashDto;
	}

	@Cacheable(value = "generalBoardSmallCacheDto", key = "'generalBoardSmallCacheDto'", unless = "#result == null")
	public GeneralBoardSmallCacheDto cachedGeneralBoard(Hackathon currentHackathon) {
		GeneralBoardSmallCacheDto generalBoardSmallCacheDto = new GeneralBoardSmallCacheDto();
		double allTotalPoints= 0.0;
		double bestScore = 0.0;

		double totalOfUserAverageAttempts =0.0;
		double totalOfUserAverageTimes =0.0;

		for (User user: currentHackathon.getUsers()){
			List<UserDashScoreBreakDownDto> usertasksBreakdowns = leaderboardService.getUserDashScoreBreakDown(user, currentHackathon);

			double totalPoints = 0.0;
			double usertotalAttempts = 0.0;
			int completedTask = 0;
			double userTotalTimeForPerTask = 0.0;
			for (UserDashScoreBreakDownDto userDashScoreBreakDownDto:usertasksBreakdowns){
				totalPoints = totalPoints + userDashScoreBreakDownDto.getPoints();
				if (userDashScoreBreakDownDto.isComplete()){
					completedTask = completedTask + 1;
					usertotalAttempts = usertotalAttempts + userDashScoreBreakDownDto.getAttempts();

					double timeTakenCal = TimeStringMinuteFunctions.parseFormattedDuration(userDashScoreBreakDownDto.getTimeTaken());
					userTotalTimeForPerTask = userTotalTimeForPerTask + timeTakenCal;

//					System.out.println("userTotalTimeForPerTask: " + userTotalTimeForPerTask);
//					System.out.println("usertotalAttempts: " + usertotalAttempts);
//					System.out.println("timeTakenCal: " + timeTakenCal);
				}
			}
			if (completedTask > 0){
				double userAverageAttempts = usertotalAttempts/completedTask;
				double userAverageTime = userTotalTimeForPerTask/completedTask;

//				System.out.println("completedTask: " + completedTask);
//				System.out.println("userAverageAttempts: " + userAverageAttempts);
//				System.out.println("userAverageTime: " + userAverageTime);

				totalOfUserAverageAttempts = totalOfUserAverageAttempts + userAverageAttempts;
				totalOfUserAverageTimes = totalOfUserAverageTimes + userAverageTime;
			}


			if (bestScore < totalPoints){
				bestScore = totalPoints;
			}
			allTotalPoints = allTotalPoints + totalPoints;
		}
		int totalUser = currentHackathon.getUsers().size();
		generalBoardSmallCacheDto.averageScore = allTotalPoints/totalUser;
		generalBoardSmallCacheDto.bestScore = bestScore;

		generalBoardSmallCacheDto.averageAttemptsPerTask = totalOfUserAverageAttempts/totalUser;
		generalBoardSmallCacheDto.averageTimePerTask = totalOfUserAverageTimes/totalUser;
//		System.out.println("totalOfUserAverageAttempts: " + totalOfUserAverageAttempts);
//		System.out.println("averageTimePerTask: " + totalOfUserAverageTimes);
//		System.out.println("GeneralBoard Stuff: " + generalBoardSmallCacheDto.toString());

		if (Double.isNaN(generalBoardSmallCacheDto.averageAttemptsPerTask)) {
			generalBoardSmallCacheDto.averageAttemptsPerTask = 0.0;
		}

		if (Double.isNaN(generalBoardSmallCacheDto.averageTimePerTask)) {
			generalBoardSmallCacheDto.averageTimePerTask = 0.0;
		}

		generalBoardSmallCacheDto.averageAttemptsPerTask = limitDouble(generalBoardSmallCacheDto.averageAttemptsPerTask);
		generalBoardSmallCacheDto.averageScore = limitDouble(generalBoardSmallCacheDto.averageScore);
		generalBoardSmallCacheDto.averageTimePerTask = limitDouble(generalBoardSmallCacheDto.averageTimePerTask);
		generalBoardSmallCacheDto.bestScore = limitDouble(generalBoardSmallCacheDto.bestScore);



		return generalBoardSmallCacheDto;
	}

	private double limitDouble(double value){
		DecimalFormat df = new DecimalFormat("#.00");

		// Format the double value
		String formattedValue = df.format(value);

		// Convert the formatted string back to a double if needed
		double formattedDouble = Double.parseDouble(formattedValue);
		return formattedDouble;
	}

}
