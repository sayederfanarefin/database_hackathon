package com.databasecourse.erfan.registration.listener;

import java.sql.*;
import java.util.UUID;
import java.util.logging.Logger;

import com.databasecourse.erfan.service.IUserService;
import com.databasecourse.erfan.persistence.model.User;
import com.databasecourse.erfan.registration.OnRegistrationCompleteEvent;
import com.databasecourse.erfan.service.RunSQLService;
import com.databasecourse.erfan.web.util.CUSTOM_DB_USER_CREATOR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    @Autowired
    private IUserService service;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;


    // API

    @Override
    public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(final OnRegistrationCompleteEvent event) {
        final User user = event.getUser();
        final String password = event.getPassword();
        final String token = UUID.randomUUID().toString();
        service.createVerificationTokenForUser(user, token);
        user.setPassword(event.getPassword());

        final SimpleMailMessage email = constructEmailMessage(event, user, token);
        mailSender.send(email);



    }

    private SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event, final User user, final String token) {
        final String recipientAddress = user.getEmail();
        final String subject = "Registration Confirmation";
        final String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
        final String message = messages.getMessage("message.regSuccLink", null, "You registered successfully. To confirm your registration, please click on the below link.", event.getLocale());
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " \r\n" + confirmationUrl);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }




}
