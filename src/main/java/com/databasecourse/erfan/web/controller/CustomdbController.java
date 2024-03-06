package com.databasecourse.erfan.web.controller;


import com.databasecourse.erfan.Constants;
import com.databasecourse.erfan.service.CustomdbService;
import com.databasecourse.erfan.web.dto.CustomdbDto;
import com.databasecourse.erfan.web.util.CheckIfAdmin;
import com.databasecourse.erfan.web.util.PagerModel;
import com.databasecourse.erfan.web.util.StringTruncator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = {"/customdb"})
public class CustomdbController {
	private static final int BUTTONS_TO_SHOW = 9;
	@Autowired
	private CustomdbService customdbService;


	@RequestMapping(value = {"/create"}, method = RequestMethod.GET)
	public Object create() {

		if (CheckIfAdmin.isAdmin()) {
			ModelAndView mv = new ModelAndView("createCustomdbAdmin");
			mv.addObject("pageTitle", "Create Custom DB");
//			mv.addObject("databasePlaceHolder", Constants.DATABASE_PLACEHOLDER);
//			mv.addObject("uiCreateInstructions", Constants.TEXT_UI_CREATE_TEXT);

			return mv;
		} else {
			return null;
		}
	}

	@RequestMapping(value = {"/test"}, method = RequestMethod.GET)
	public Object test( ) {

		if (CheckIfAdmin.isAdmin()) {
			ModelAndView mv = new ModelAndView("customDbQueryToolAdmin");
			mv.addObject("pageTitle", "Custom DB Query Tool");
			mv.addObject("dbList", customdbService.getAllCustomDBs());
			mv.addObject("uiCreateInstructions", Constants.TEXT_UI_TEST_QUERY);

			return mv;
		} else {
			return null;
		}
	}

	@RequestMapping(value = {"/test/{itemid}"}, method = RequestMethod.GET)
	public Object testWithDbID(@PathVariable("itemid") Long itemid) {

		if (CheckIfAdmin.isAdmin()) {
			ModelAndView mv = new ModelAndView("customDbQueryToolAdmin");
			mv.addObject("pageTitle", "Custom DB Query Tool");
			mv.addObject("dbList", customdbService.getAllCustomDBs());
			mv.addObject("uiCreateInstructions", Constants.TEXT_UI_TEST_QUERY);
			mv.addObject("selectedDbFromTaskView", itemid);

			return mv;
		} else {
			return null;
		}
	}


	@RequestMapping(value = {"/view/{itemid}"}, method = RequestMethod.GET)
	public ModelAndView view(@PathVariable("itemid") Long itemid) {

		if (CheckIfAdmin.isAdmin()) {
			ModelAndView mv = new ModelAndView("viewCustomdbAdmin");

			mv.addObject("pageTitle", "Custom DB Details");
			mv.addObject("customDb", customdbService.getCustondbDto(itemid));
			return mv;
		} else {
			return null;
		}
	}

	@RequestMapping(value = {"/edit/{itemid}"}, method = RequestMethod.GET)
	public ModelAndView edit(@PathVariable("itemid") Long itemid) {

		if (CheckIfAdmin.isAdmin()) {

			ModelAndView mv = new ModelAndView("editCustomdbAdmin");

			mv.addObject("pageTitle", "Edit Custom DB");
			mv.addObject("customDb", customdbService.getCustondbDto(itemid));
			return mv;
		} else {
			return null;
		}
	}

	@RequestMapping(value = {"/view/all"}, method = RequestMethod.GET)
	public ModelAndView viewall(@RequestParam(value = "page", required = false, defaultValue = "0") int page) {
		if (CheckIfAdmin.isAdmin()) {
			ModelAndView modelandview = new ModelAndView("viewAllCustomdbAdmin");
			modelandview.addObject("pageTitle", "All Custom DB");

			Page<CustomdbDto> customdbDtos = customdbService.getAllCustomDBs(page);

			for (CustomdbDto customdbDto : customdbDtos) {
				customdbDto.setDescription(StringTruncator.truncateString(customdbDto.getDescription()));
				customdbDto.setQuery(StringTruncator.truncateString(customdbDto.getQuery()));
			}


			modelandview.addObject("data", customdbDtos);
			//System.out.println(employeeList.getTotalPages()+" "+employeeList.getTotalElements());
			PagerModel pager = new PagerModel(customdbDtos.getTotalPages(), customdbDtos.getNumber(), BUTTONS_TO_SHOW);
			modelandview.addObject("pager", pager);
			return modelandview;
		} else {
			return null;
		}

	}


	@RequestMapping(value = {"/view/all/{page}"}, method = RequestMethod.GET)
	public ModelAndView viewallByPage(@PathVariable(value = "page", required = true) int page) {
		if (CheckIfAdmin.isAdmin()) {
			ModelAndView modelandview = new ModelAndView("viewAllCustomdbAdmin");
			modelandview.addObject("pageTitle", "All Custom DB");
			System.out.println("------- page --------: " + page);
			Page<CustomdbDto> customDbList = customdbService.getAllCustomDBs(page);

			for (CustomdbDto customdbDto : customDbList) {
				customdbDto.setDescription(StringTruncator.truncateString(customdbDto.getDescription()));
				customdbDto.setQuery(StringTruncator.truncateString(customdbDto.getQuery()));
			}

			modelandview.addObject("data", customDbList);
			//System.out.println(employeeList.getTotalPages()+" "+employeeList.getTotalElements());
			PagerModel pager = new PagerModel(customDbList.getTotalPages(), customDbList.getNumber(), BUTTONS_TO_SHOW);
			modelandview.addObject("pager", pager);
			return modelandview;
		} else {
			return null;
		}

	}

}
