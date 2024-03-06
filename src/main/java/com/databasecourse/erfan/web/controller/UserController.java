package com.databasecourse.erfan.web.controller;

import com.databasecourse.erfan.Constants;
import com.databasecourse.erfan.persistence.dao.RoleRepository;
import com.databasecourse.erfan.persistence.dtoConverters.UserDtoModelConverrter;
import com.databasecourse.erfan.persistence.model.User;
import com.databasecourse.erfan.security.ActiveUserStore;
import com.databasecourse.erfan.service.IUserService;
import com.databasecourse.erfan.web.dto.UserDisplayDto;
import com.databasecourse.erfan.web.util.CheckIfAdmin;
import com.databasecourse.erfan.web.util.PagerModel;
import com.databasecourse.erfan.web.util.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;

@Controller
public class UserController {
    private static final int BUTTONS_TO_SHOW = 9;
    @Autowired
    ActiveUserStore activeUserStore;

    @Autowired
    IUserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserDtoModelConverrter userDtoModelConverrter;

    @GetMapping("/loggedUsers")
    public String getLoggedUsers(final Locale locale, final Model model) {
        model.addAttribute("users", activeUserStore.getUsers());
        return "users";
    }

    @GetMapping("/loggedUsersFromSessionRegistry")
    public String getLoggedUsersFromSessionRegistry(final Locale locale, final Model model) {
        model.addAttribute("users", userService.getUsersFromSessionRegistry());
        return "users";
    }

    @RequestMapping(value = {"/users/create"}, method = RequestMethod.GET)
    public ModelAndView createUsersAdmin() {
        if (CheckIfAdmin.isAdmin()) {
            ModelAndView mv = new ModelAndView("createUserAdmin");
            mv.addObject("pageTitle", "Create User");
            mv.addObject("dbPrefix", Constants.USER_DB_NAME_PREFIX);
            mv.addObject("emailPostfix", Constants.EMAIL_POSTFIX);
            mv.addObject("autoTeamName", userService.generateValidTeamName());
            mv.addObject("randomPassword", PasswordGenerator.generateRandomPassword(Constants.RANDOM_PASSWORD_LENGTH));
//            mv.addObject("randomDatabaseName", RandomDatabaseNameGenerator.generateRandomDatabaseName());
            return mv;
        } else {
            return null;
        }
    }

    @RequestMapping(value = {"/users/create/batch"}, method = RequestMethod.GET)
    public ModelAndView createUsersBatchAdmin() {
        if (CheckIfAdmin.isAdmin()) {
            ModelAndView mv = new ModelAndView("createUserAdminBatch");
            mv.addObject("pageTitle", "Create User Batch");

            return mv;
        } else {
            return null;
        }
    }

    @RequestMapping(value = {"/users/edit/{itemid}"}, method = RequestMethod.GET)
    public ModelAndView editUser(@PathVariable("itemid") Long itemid) {
        if (CheckIfAdmin.isAdmin()) {
            User user = userService.getUserByID(itemid).get();

            ModelAndView mv = new ModelAndView("updateUserAdmin");
            mv.addObject("pageTitle", "Create User");
            mv.addObject("dbUserName", user.getDatabaseUserName());
            mv.addObject("email", user.getEmail());
            mv.addObject("dbName", user.getDatabaseName());
            mv.addObject("firstName", user.getFirstName());
            mv.addObject("lastName", user.getLastName());
            mv.addObject("teamName", user.getTeamName());
            mv.addObject("dbPrefix", Constants.USER_DB_NAME_PREFIX);
            mv.addObject("emailPostfix", Constants.EMAIL_POSTFIX);
            mv.addObject("autoTeamName", userService.generateValidTeamName());
            mv.addObject("randomPassword", PasswordGenerator.generateRandomPassword(Constants.RANDOM_PASSWORD_LENGTH));
//            mv.addObject("randomDatabaseName", RandomDatabaseNameGenerator.generateRandomDatabaseName());
            return mv;
        } else {
            return null;
        }
    }

    @RequestMapping(value = {"/settings"}, method = RequestMethod.GET)
    public ModelAndView updatePassword() {

        ModelAndView mv = new ModelAndView("userSettings");
        mv.addObject("pageTitle", "User Settings");
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        mv.addObject("user", userDtoModelConverrter.convert(authUser));
//            ModelAndView mv = new ModelAndView("updatePassword");
        return mv;

    }


    @RequestMapping(value = {"/users/view/all"}, method = RequestMethod.GET)
    public ModelAndView showAllUsers(@RequestParam(value = "page", required = false, defaultValue = "0") int page
    ) {
        if (CheckIfAdmin.isAdmin()) {
            ModelAndView modelandview = new ModelAndView("viewAllUserAdmin");
            modelandview.addObject("pageTitle", "All Users");


            Page<UserDisplayDto> usersList = userService.getAllUsers(page);
            modelandview.addObject("data", usersList);
            //System.out.println(employeeList.getTotalPages()+" "+employeeList.getTotalElements());
            PagerModel pager = new PagerModel(usersList.getTotalPages(), usersList.getNumber(), BUTTONS_TO_SHOW);
            modelandview.addObject("pager", pager);
            return modelandview;
        } else {
            return null;
        }
    }


    @RequestMapping(value = {"/users/view/all/{page}"}, method = RequestMethod.GET)
    public ModelAndView viewallByPage(@PathVariable(value = "page", required = true) int page
    ) {
        System.out.println("Am i here?");
        if (CheckIfAdmin.isAdmin()) {
            ModelAndView modelandview = new ModelAndView("viewAllUserAdmin");
            modelandview.addObject("pageTitle", "All Users");


            Page<UserDisplayDto> usersList = userService.getAllUsers(page);
            modelandview.addObject("data", usersList);
            //System.out.println(employeeList.getTotalPages()+" "+employeeList.getTotalElements());
            PagerModel pager = new PagerModel(usersList.getTotalPages(), usersList.getNumber(), BUTTONS_TO_SHOW);
            modelandview.addObject("pager", pager);
            return modelandview;
        } else {
            return null;
        }
    }

    @RequestMapping(value = {"users/view/{itemid}"}, method = RequestMethod.GET)
    public ModelAndView view(@PathVariable("itemid") Long itemid) {

        if (CheckIfAdmin.isAdmin()) {
            User user = userService.getUserByID(itemid).get();

            ModelAndView mv = new ModelAndView("viewUserAdmin");

            mv.addObject("pageTitle", "User Details");
            mv.addObject("user", userDtoModelConverrter.convert(user));


            if (user.getRoles().contains(roleRepository.findByName(Constants.ADMIN_ROLE))){
                mv.addObject("admin", true);
            } else {
                mv.addObject("admin", false);
            }
            return mv;
        } else {
            return null;
        }
    }


}
