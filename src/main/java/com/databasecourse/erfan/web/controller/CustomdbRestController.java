package com.databasecourse.erfan.web.controller;

import com.databasecourse.erfan.persistence.model.Customdb;
import com.databasecourse.erfan.persistence.model.Hackathon;
import com.databasecourse.erfan.persistence.model.User;
import com.databasecourse.erfan.security.ISecurityUserService;
import com.databasecourse.erfan.service.*;
import com.databasecourse.erfan.web.dto.*;
import com.databasecourse.erfan.web.util.GenericResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = {"/customdb"})
public class CustomdbRestController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private ICustomdbService customdbService;

    @Autowired
    private ISecurityUserService securityUserService;


    public CustomdbRestController() {
        super();
    }

    // Registration
    @PostMapping("/create")
    public GenericResponse createCustomDB(@Valid final CustomdbDto customdbDto) {
        LOGGER.debug("Create task with information: {}", customdbDto);
        System.out.println("---------- customdbDto create -----------");
        System.out.println(customdbDto.toString());
        System.out.println("---------- customdbDto end create -----------");
        String stat = customdbService.createCustomDB(customdbDto);
        return new GenericResponse(stat);
    }

    @PostMapping("/edit")
    public GenericResponse editCustomDB(@Valid final CustomdbDto customdbDto) {
        LOGGER.debug("Edit task with information: {}", customdbDto);
        System.out.println("---------- customdbDto update -----------");
        System.out.println(customdbDto.toString());
        String returned = customdbService.updateCustomDB(customdbDto);
        return new GenericResponse(returned);
    }

    @PostMapping("/delete/{itemid}")
    public GenericResponse deleteCustomDB(@PathVariable("itemid") Long itemid) {
        customdbService.deleteCustomdb(itemid);
        return new GenericResponse("success");
    }
    public static boolean isWhitespaceString(String input) {
        // Use a regular expression to check if the input string consists of only spaces, tabs, or newlines.
        Pattern pattern = Pattern.compile("^\\s*$");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
    @PostMapping("/test")
    public CustomDBTestQueryDto testCustomDB(@Valid final CustomDBTestQueryDto customDBTestQueryDto) {
//        LOGGER.debug("Create task with information: {}", customDBTestQueryDto);
        System.out.println("---------- custom db query test -----------");
        System.out.println(customDBTestQueryDto.toString());

        String[] queries = customDBTestQueryDto.getTestQuery().split(";");

        String retruned = "";
        for (String query : queries) {
            if (!isWhitespaceString(query)){
                query = query + ";";
                System.out.println("Query unit: " + query);
                retruned = customdbService.runQuery(customDBTestQueryDto.getDbId(), query);
            }
        }

        customDBTestQueryDto.setTestQueryResults(retruned);
        return customDBTestQueryDto;
    }


    @PostMapping("/test/resetDB")
    public boolean resetTempDB(@RequestBody ResetCustomDBDto resetCustomDBDto) {

        System.out.println("--- reset temp db ----" + resetCustomDBDto.toString());
        customdbService.resetDb(resetCustomDBDto.getDbId());
//        return taskService.resetTempDB();
        return false;
    }



}
