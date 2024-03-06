package com.databasecourse.erfan.service;

public interface IRandomFileNameGenerator {
    public String generateRandomTemporaryFileName(String ext);
    public String generateRandomFileName(String ext);
}
