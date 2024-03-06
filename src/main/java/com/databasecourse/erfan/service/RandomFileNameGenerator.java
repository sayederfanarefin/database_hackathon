package com.databasecourse.erfan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;


@Service
public class RandomFileNameGenerator implements IRandomFileNameGenerator{
    @Autowired
    private ResourceLoader resourceLoader;

    public Path createTempDirectory() {
        try {
            Path tempDir = Files.createTempDirectory("mytempdir");
            return tempDir;
        } catch ( Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public String generateRandomTemporaryFileName(String ext)  {
        // Get the system's temporary directory path
//        String tempDirPath = System.getProperty("java.io.tmpdir");
        System.out.println("generateRandomTemporaryFileName called");

        Path tempDirPathx = createTempDirectory();

        String absPath = tempDirPathx.toAbsolutePath().toString();

        System.out.println("Absolute path to project: " + absPath);
//        String tempDirPath = absPath + File.separator + TEMP_SQL_BACKUP_LOCATION;
        // Generate a random UUID to ensure uniqueness
        String randomUUID = UUID.randomUUID().toString();

        // Combine the temporary directory and random UUID to create the temporary file path
        String temporaryFilePath = absPath + File.separator + "RandomFile_" + randomUUID + "." + ext;

        return temporaryFilePath;
    }
    @Override
    public String generateRandomFileName(String ext) {

        String randomUUID = UUID.randomUUID().toString();

        // Combine the temporary directory and random UUID to create the temporary file path
        String temporaryFilePath =  randomUUID + "." + ext;

        return temporaryFilePath;
    }

    public static String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return null;
        }
    }
}
