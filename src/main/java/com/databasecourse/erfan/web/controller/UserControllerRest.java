package com.databasecourse.erfan.web.controller;

import com.databasecourse.erfan.Constants;
import com.databasecourse.erfan.persistence.dao.UserRepository;
import com.databasecourse.erfan.persistence.dtoConverters.UserDtoModelConverrter;
import com.databasecourse.erfan.persistence.model.User;
import com.databasecourse.erfan.security.ActiveUserStore;
import com.databasecourse.erfan.service.IUserService;
import com.databasecourse.erfan.service.storage.StorageService;
import com.databasecourse.erfan.web.dto.UserBatchCreateDto;
import com.databasecourse.erfan.web.dto.UserDisplayDto;
import com.databasecourse.erfan.web.util.CheckIfAdmin;
import com.databasecourse.erfan.web.util.GenericResponse;
import com.databasecourse.erfan.web.util.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import static com.databasecourse.erfan.Constants.DUMMY_PROFILE_PIC;

@RestController
public class UserControllerRest {
    @Autowired
    StorageService storageService;

    @Autowired
    ActiveUserStore activeUserStore;

    @Autowired
    IUserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserDtoModelConverrter userDtoModelConverrter;

    @PostMapping("/settings/update")
    public UserDisplayDto uploadFileAndString(
            @RequestParam(value = "profileImageUpload", required = false) MultipartFile profileImageUpload,
            @RequestParam("teamName") String teamName) {

        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userr = userService.findUserByEmail(authUser.getEmail());
        userr.setTeamName(teamName);

        if ( profileImageUpload!= null) {
            // Process the file and string inputs
            System.out.println("Saving pro pic");
            String pathToFile = storageService.store(profileImageUpload, false);
            System.out.println("path to pro pic: "+ pathToFile);
            userr.setPathToProPic(pathToFile);
        }

        userService.saveRegisteredUser(userr);
        return userDtoModelConverrter.convert(userr);
    }

    @PostMapping("/user/delete/{itemid}")
    public GenericResponse deleteUser(@PathVariable("itemid") Long itemid) {
        System.out.println("---------- user delete -----------");
        userService.deleteUser(userService.getUserByID(itemid).get());
        return new GenericResponse("success");
    }

    @PostMapping("/user/resetprofilepicture/{itemid}")
    public GenericResponse resetUserProfile(@PathVariable("itemid") Long itemid) {
        System.out.println("---------- user reset profile picture -----------");

        User user = userService.getUserByID(itemid).get();
        user.setPathToProPic(DUMMY_PROFILE_PIC);
        userRepository.save(user);
        return new GenericResponse("success");
    }

    @PostMapping("/user/resetpassword/{itemid}")
    public GenericResponse resetPasswordByAdmin(@PathVariable("itemid") Long itemid) {
        System.out.println("---------- user resetpassword -----------");

        if (CheckIfAdmin.isAdmin()){
            User user = userService.getUserByID(itemid).get();
            String newPassword = PasswordGenerator.generateRandomPassword(Constants.RANDOM_PASSWORD_LENGTH);
            userService.changeUserPassword(user, newPassword);
            return new GenericResponse(newPassword);
        }

        return new GenericResponse("Failed, not admin.");
    }


    @PostMapping("/users/create/batch")
    public UserBatchCreateDto createUsersBatchAdmin(@RequestParam(value = "excelFileUpload", required = false) MultipartFile excelFileUpload) {

        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (CheckIfAdmin.isAdmin()) {
            System.out.println("I am admin! Yo!");
            String fileName = excelFileUpload.getOriginalFilename();
            return userService.createUserInBatch(fileName);
        } else {
            return null;
        }
    }
}
