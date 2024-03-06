package com.databasecourse.erfan.persistence.model;

import java.util.Collection;

import javax.persistence.*;

import com.databasecourse.erfan.Constants;
import org.jboss.aerogear.security.otp.api.Base32;

@Entity
@Table(name = "user_account")
public class User {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;
    private String teamName;

    private String email;

    @Column(length = 60)
    private String password;

    private boolean enabled;

    private boolean isUsing2FA;

    private String secret;
    private String databaseName;
    private String databaseUserName;

    private String pathToProPic;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<Usertask> userTasks;
    @ManyToMany(fetch = FetchType.LAZY, cascade= {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "users_hackathons", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "hackathon_id", referencedColumnName = "id"))
    private Collection<Hackathon> hackathons;
    @ManyToMany(fetch = FetchType.EAGER, cascade= CascadeType.ALL)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @PreRemove
    private void removeUsers() {
        roles.clear();
        hackathons.clear();
    }

    public User() {
        super();
        this.secret = Base32.random();
        this.enabled = false;
        this.pathToProPic= Constants.DUMMY_PROFILE_PIC;
    }

    public String getPathToProPic() {
        return pathToProPic;
    }

    public void setPathToProPic(String pathToProPic) {
        this.pathToProPic = pathToProPic;
    }

    public Collection<Usertask> getUserTasks() {
        return userTasks;
    }

    public void setUserTasks(Collection<Usertask> userTasks) {
        this.userTasks = userTasks;
    }

    public String getDatabaseUserName() {
        return databaseUserName;
    }

//    @ManyToMany(fetch = FetchType.LAZY, cascade= CascadeType.MERGE)
//    @JoinTable(name = "users_tasks", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"))
//    private Collection<Task> tasks;

    public void setDatabaseUserName(String databaseUserName) {
        this.databaseUserName = databaseUserName;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String username) {
        this.email = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(final Collection<Role> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isUsing2FA() {
        return isUsing2FA;
    }

    public void setUsing2FA(boolean isUsing2FA) {
        this.isUsing2FA = isUsing2FA;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((getEmail() == null) ? 0 : getEmail().hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User user = (User) obj;
        return getEmail().equals(user.getEmail());
    }

    @Override
    public String toString() {
        String builder = "User [id=" +
                id +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", email=" + email +
                ", enabled=" + enabled +
                ", isUsing2FA=" + isUsing2FA +
                ", teamName=" + teamName +
                ", secret=" + secret +
                ", roles=" + roles +
                "]";
        return builder;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }


    public Collection<Hackathon> getHackathons() {
        return hackathons;
    }

    public void setHackathons(Collection<Hackathon> hackathons) {
        this.hackathons = hackathons;
    }

    public void addHackathon(Hackathon hackathon){
        hackathons.add(hackathon);
    }

//    public Collection<Task> getTasks() {
//        return tasks;
//    }
//
//    public void setTasks(Collection<Task> tasks) {
//        this.tasks = tasks;
//    }
}