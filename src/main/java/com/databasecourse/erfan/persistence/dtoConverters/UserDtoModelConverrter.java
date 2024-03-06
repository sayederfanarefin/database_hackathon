package com.databasecourse.erfan.persistence.dtoConverters;

import com.databasecourse.erfan.persistence.model.User;
import com.databasecourse.erfan.web.dto.UserDisplayDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;

@Service
public class UserDtoModelConverrter {

    public UserDisplayDto convert(User user){
        UserDisplayDto userDisplayDto
                 = new UserDisplayDto();
        userDisplayDto.setTeamName(user.getTeamName());
        userDisplayDto.setFirstName(user.getFirstName());
        userDisplayDto.setLastName(user.getLastName());
        userDisplayDto.setDatabaseName(user.getDatabaseName());
        userDisplayDto.setDatabaseUserName(user.getDatabaseUserName());
        userDisplayDto.setEmail(user.getEmail());
        userDisplayDto.setId(user.getId());
        userDisplayDto.setPathToProPic(user.getPathToProPic());
        return userDisplayDto;

    }

    public User convert(UserDisplayDto user){
        User userDisplayDto  = new User();
        userDisplayDto.setTeamName(user.getTeamName());
        userDisplayDto.setFirstName(user.getFirstName());
        userDisplayDto.setLastName(user.getLastName());
        userDisplayDto.setDatabaseName(user.getDatabaseName());
        userDisplayDto.setDatabaseUserName(user.getDatabaseUserName());
        userDisplayDto.setEmail(user.getEmail());
        userDisplayDto.setId(user.getId());
        userDisplayDto.setPathToProPic(user.getPathToProPic());

        return userDisplayDto;

    }

    public List<UserDisplayDto> convertList(List<User> users){
        List<UserDisplayDto> userDtos = new ArrayList<>();
        for (User user : users) {
            userDtos.add(convert(user));
        }
        return userDtos;
    }

    public List<User> convertListUser(List<UserDisplayDto> users){
        List<User> userDtos = new ArrayList<>();
        for (UserDisplayDto user : users) {
            userDtos.add(convert(user));
        }
        return userDtos;
    }


}
