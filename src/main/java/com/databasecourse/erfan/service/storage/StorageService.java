package com.databasecourse.erfan.service.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    String store(MultipartFile file, boolean isAdmin);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

    String getAdminFileStoragePath(String extension);

    String getURLFromAdminFileStoragePath(String actualFilePathInWeb);
}