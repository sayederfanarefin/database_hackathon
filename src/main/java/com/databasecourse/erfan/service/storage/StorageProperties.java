package com.databasecourse.erfan.service.storage;

import com.databasecourse.erfan.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = Constants.UPLOAD_LOCATION_COMMON;
//    private String location = Constants.UPLOAD_LOCATION;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}