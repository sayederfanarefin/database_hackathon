package com.databasecourse.erfan.web.dto;

import com.databasecourse.erfan.validation.PasswordMatches;
import com.databasecourse.erfan.validation.ValidEmail;
import com.databasecourse.erfan.validation.ValidPassword;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@PasswordMatches
public class UserDto {
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
    @Size(min = 1, message = "{Size.userDto.databaseName}")
    private String databaseName;

    @NotNull
    @Size(min = 1, message = "{Size.userDto.databaseUserName}")
    private String databaseUserName;

    @ValidPassword
    private String password;

    @ValidEmail
    @NotNull
    @Size(min = 1, message = "{Size.userDto.email}")
    private String email;

    private String pathToProPic;


    public String getPathToProPic() {
        return pathToProPic;
    }

    public void setPathToProPic(String pathToProPic) {
        this.pathToProPic = pathToProPic;
    }

    private boolean isUsing2FA;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

//    public String getMatchingPassword() {
//        return matchingPassword;
//    }

//    public void setMatchingPassword(final String matchingPassword) {
//        this.matchingPassword = matchingPassword;
//    }

    public boolean isUsing2FA() {
        return isUsing2FA;
    }

    public void setUsing2FA(boolean isUsing2FA) {
        this.isUsing2FA = isUsing2FA;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", teamName='" + teamName + '\'' +
                ", databaseName='" + databaseName + '\'' +
                ", databaseUserName='" + databaseUserName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", pathToProPic='" + pathToProPic + '\'' +
                ", isUsing2FA=" + isUsing2FA +
                ", role=" + role +
                '}';
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
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
