package com.databasecourse.erfan.web.dto;

import java.util.ArrayList;
import java.util.List;

public class UserBatchCreateDto {


    private String creationNotes;
    private boolean created;
    private List<UserBatchCreateItemDto> userBatchCreateItemDtoList = new ArrayList<>();

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

    public List<UserBatchCreateItemDto> getUserBatchCreateItemDtoList() {
        return userBatchCreateItemDtoList;
    }

    public void setUserBatchCreateItemDtoList(List<UserBatchCreateItemDto> userBatchCreateItemDtoList) {
        this.userBatchCreateItemDtoList = userBatchCreateItemDtoList;
    }
}
