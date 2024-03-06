package com.databasecourse.erfan.registration;

import java.util.Locale;

import com.databasecourse.erfan.persistence.model.User;
import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private final String appUrl;
    private final Locale locale;
    private final User user;
    private final String password;

    public OnRegistrationCompleteEvent(final String password, final User user, final Locale locale, final String appUrl) {
        super(user);
        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
        this.password = password;
    }

    //

    public String getAppUrl() {
        return appUrl;
    }

    public Locale getLocale() {
        return locale;
    }

    public User getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
