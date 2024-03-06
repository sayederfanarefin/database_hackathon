package com.databasecourse.erfan.service.storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import com.databasecourse.erfan.service.RandomFileNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import static com.databasecourse.erfan.Constants.UPLOAD_LOCATION_ADMIN;
import static com.databasecourse.erfan.Constants.UPLOAD_LOCATION_COMMON;
import static com.databasecourse.erfan.service.RandomFileNameGenerator.*;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    private RandomFileNameGenerator randomFileNameGenerator;
    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public String store(MultipartFile file, boolean isAdmin) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }

            String randomFIleName = randomFileNameGenerator.generateRandomFileName(getFileExtension(file.getOriginalFilename()));

            String prefix = "";
            String midfix = "";
            if (isAdmin){
                prefix = UPLOAD_LOCATION_ADMIN;
                midfix = "admin";
            } else {
                prefix = UPLOAD_LOCATION_COMMON;
                midfix = "user";
            }
            String actualFilePathInWeb =  prefix+ File.separator + randomFIleName;

            Path actualFilePathInWebPath = Paths.get(actualFilePathInWeb);


            try (InputStream inputStream = file.getInputStream()) {

                Files.copy(inputStream, actualFilePathInWebPath, StandardCopyOption.REPLACE_EXISTING);

                String reconstructedFilePathInWeb = "/files/"+midfix+"/" + randomFIleName;
                return reconstructedFilePathInWeb;
            }


        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }
    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }
    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException( "Could not read file: " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public String getAdminFileStoragePath(String extension) {
        String randomFIleName = randomFileNameGenerator.generateRandomFileName(extension);
        String actualFilePathInWeb =  UPLOAD_LOCATION_ADMIN+ File.separator + randomFIleName;
        return actualFilePathInWeb;
    }

    @Override
    public String getURLFromAdminFileStoragePath(String actualFilePathInWeb) {
        Path actualFilePathInWebPath = Paths.get(actualFilePathInWeb);
        String randomFIleName = actualFilePathInWebPath.getFileName().toString();
        String reconstructedFilePathInWeb = "/files/admin/" + randomFIleName;
        return reconstructedFilePathInWeb;

    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}