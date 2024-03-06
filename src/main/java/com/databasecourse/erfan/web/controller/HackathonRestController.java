package com.databasecourse.erfan.web.controller;


import com.databasecourse.erfan.service.HackathonService;
import com.databasecourse.erfan.service.UserService;
import com.databasecourse.erfan.web.dto.HackathonDto;
import com.databasecourse.erfan.web.util.CheckIfAdmin;
import com.databasecourse.erfan.web.util.GenericResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping(value={ "/hackathon"})
public class HackathonRestController {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
   private HackathonService hackathonService;

   @Autowired
   private UserService userService;


//   @Autowired
//   private HackthonService hackathonService;

	@PostMapping("/create")
	public GenericResponse createHackathon(@Valid final HackathonDto hackathonDto) {

		System.out.println("---------- hackathon create -----------");
		if (CheckIfAdmin.isAdmin()) {
			hackathonService.createhackathon(hackathonDto);
			return new GenericResponse("success");
		} else {
			return new GenericResponse("failed");
		}
	}

	@PostMapping("/delete/{itemid}")
	public GenericResponse deleteHackathon(@PathVariable("itemid") Long itemid) {
		System.out.println("---------- hackathon delete -----------");
		hackathonService.deleteHackathon(itemid);

		if (CheckIfAdmin.isAdmin()) {
		return new GenericResponse("success");
		} else {
			return new GenericResponse("failed");
		}
	}

	@PostMapping("/clear/submission/{userId}/{taskId}/{hackathonId}")
	public GenericResponse clearSubmissionUserTask(@PathVariable("userId") Long userId, @PathVariable("taskId") Long taskId, @PathVariable("hackathonId") Long hackathonId) {
		System.out.println("---------- hackathon clear user submission -----------");

		if (CheckIfAdmin.isAdmin()) {
			boolean returned = hackathonService.clearSubmissionUser(taskId, userId, hackathonId);
			System.out.println("Is admin yo");
			if (returned){
				return new GenericResponse("success");
			}else {
				return new GenericResponse("failed");
			}
		} else {
			System.out.println("Not admin");
			return new GenericResponse("failed");
		}
	}

	@PostMapping("/clear/submission/{taskId}/{hackathonId}")
	public GenericResponse clearSubmissioForAllUsres(@PathVariable("taskId") Long taskId, @PathVariable("hackathonId") Long hackathonId) {
		System.out.println("---------- hackathon clear all user submission -----------");

		if (CheckIfAdmin.isAdmin()) {
			boolean returned = hackathonService.clearSubmissionUserAll(taskId, hackathonId);
			if (returned){
				return new GenericResponse("success");
			}else {
				return new GenericResponse("failed");
			}

		} else {
			return new GenericResponse("failed");
		}
	}

	@PostMapping("/download/grading/{hackathonId}")
	public GenericResponse downloadGrading(@PathVariable("hackathonId") Long hackathonId) {
		System.out.println("---------- download grading -----------");
		String returnedText = hackathonService.generateGradeBook(hackathonId);
		if (CheckIfAdmin.isAdmin()) {
			return new GenericResponse(returnedText);
		} else {
			return new GenericResponse("failed");
		}
	}


}
