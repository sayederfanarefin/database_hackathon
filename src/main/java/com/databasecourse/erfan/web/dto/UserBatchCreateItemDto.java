package com.databasecourse.erfan.web.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

public class UserBatchCreateItemDto {


    private String creationNotes;
    private boolean created;
    private UserDto user;

    public String getCreationNotes() {
        return creationNotes;
    }

    public void setCreationNotes(String creationNotes) {
        this.creationNotes = creationNotes;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
