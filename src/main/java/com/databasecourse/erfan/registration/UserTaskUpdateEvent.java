package com.databasecourse.erfan.registration;

import com.databasecourse.erfan.persistence.model.Hackathon;
import com.databasecourse.erfan.persistence.model.Task;
import com.databasecourse.erfan.persistence.model.User;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@SuppressWarnings("serial")
public class UserTaskUpdateEvent extends ApplicationEvent {

    User user;
    Task task;

    Hackathon hackathon;

    public UserTaskUpdateEvent(final User user, final Task task, final Hackathon hackathon) {
        super(user);
        this.user = user;
        this.task = task;
        this.hackathon = hackathon;

    }

    public User getUser() {
        return user;
    }

    public Task getTask() {
        return task;
    }

    public Hackathon getHackathon() {
        return hackathon;
    }
}
