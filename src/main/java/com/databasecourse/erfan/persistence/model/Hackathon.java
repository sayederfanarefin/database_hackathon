package com.databasecourse.erfan.persistence.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
public class Hackathon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean leaderBoardEnabled;
    private boolean generalBoardEnabled;

    private String title;
    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @Column(columnDefinition = "LONGTEXT")
    private String privateNotes;

    @ManyToMany() //xfetch = FetchType.EAGER, cascade= CascadeType.ALL)
    @JoinTable(name = "hackathons_tasks", joinColumns = @JoinColumn(name = "hackathon_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"))
    private Collection<Task> tasks;

    @ManyToMany(mappedBy = "hackathons", cascade= CascadeType.ALL)
    private Collection<User> users;

    public String getPrivateNotes() {
        return privateNotes;
    }

    public void setPrivateNotes(String privateNotes) {
        this.privateNotes = privateNotes;
    }

    @OneToOne(mappedBy = "hackathon", cascade = CascadeType.ALL)
    private Leaderboard leaderboard;

//    @OneToMany(fetch = FetchType.LAZY, cascade= CascadeType.DETACH)
//    private List<User> users;

    @PreRemove
    private void removeHackathons() {
        users.clear();
        tasks.clear();
    }

    public Hackathon() {
        super();
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }



    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }


    public Collection<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public Leaderboard getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
    }

    @Override
    public String toString() {
        return "Hackathon{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", tasks=" + tasks +
                ", users=" + users +
                ", leaderboard=" + leaderboard +
                '}';
    }

    public boolean isLeaderBoardEnabled() {
        return leaderBoardEnabled;
    }

    public void setLeaderBoardEnabled(boolean leaderBoardEnabled) {
        this.leaderBoardEnabled = leaderBoardEnabled;
    }

    public boolean isGeneralBoardEnabled() {
        return generalBoardEnabled;
    }

    public void setGeneralBoardEnabled(boolean generalBoardEnabled) {
        this.generalBoardEnabled = generalBoardEnabled;
    }
}
