package com.databasecourse.erfan.web.dto;

import com.databasecourse.erfan.validation.PasswordMatches;
import com.databasecourse.erfan.validation.ValidEmail;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@PasswordMatches
public class UserDisplayDto {

    private Long id;
    @NotNull
    @Size(min = 1, message = "{Size.userDto.firstName}")
    private String firstName;

    @NotNull
    @Size(min = 1, message = "{Size.userDto.lastName}")
    private String lastName;

    @NotNull
    @Size(min = 1, message = "{Size.userDto.teamName}")
    private String teamName;

    @NotNull
    @Size(min = 1, message = "{Size.userDto.teamName}")
    private String databaseName;

    @ValidEmail
    @NotNull
    @Size(min = 1, message = "{Size.userDto.email}")
    private String email;

    @NotNull
    @Size(min = 1, message = "{Size.userDto.databaseUserName}")
    private String databaseUserName;

    private String pathToProPic;


    public String getPathToProPic() {
        return pathToProPic;
    }

    public void setPathToProPic(String pathToProPic) {
        this.pathToProPic = pathToProPic;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    private Integer role;

    public Integer getRole() {
        return role;
    }

    public void setRole(final Integer role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }


//    public String getMatchingPassword() {
//        return matchingPassword;
//    }

//    public void setMatchingPassword(final String matchingPassword) {
//        this.matchingPassword = matchingPassword;
//    }


    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        String builder = "UserDto [firstName=" +
                firstName +
                ", lastName=" +
                lastName +
                ", email=" +
                email +
                ", teamName=" +
                teamName +
                ", role=" +
                role + "]";
        return builder;
    }
    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }

    public String getDatabaseUserName() {
        return databaseUserName;
    }

    public void setDatabaseUserName(String databaseUserName) {
        this.databaseUserName = databaseUserName;
    }
}
