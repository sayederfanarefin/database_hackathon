package com.databasecourse.erfan.security;

public interface ISecurityUserService {

    String validatePasswordResetToken(String token);

}
