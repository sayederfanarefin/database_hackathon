package com.databasecourse.erfan.registration;

import com.databasecourse.erfan.persistence.model.Hackathon;
import com.databasecourse.erfan.persistence.model.Task;
import com.databasecourse.erfan.persistence.model.User;
import org.springframework.context.ApplicationEvent;

import java.util.List;
import java.util.Locale;

@SuppressWarnings("serial")
public class OnLeaderboardCheckEvent extends ApplicationEvent {

    private final List<User> hackathonUsers;
    private final List<Task> tasks;
    private final Hackathon hackathon;

    public OnLeaderboardCheckEvent(final List<User> hackathonUsers, final List<Task> tasks, final Hackathon hackathon) {
        super(hackathon);
        this.hackathon = hackathon;
        this.hackathonUsers = hackathonUsers;
        this.tasks = tasks;
    }

    public List<User> getHackathonUsers() {
        return hackathonUsers;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Hackathon getHackathon() {
        return hackathon;
    }
}
