package com.databasecourse.erfan.persistence.model;

import javax.persistence.*;
import java.util.Formatter;

@Entity
public class Leaderelement implements Comparable<Leaderelement> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fulleName;

    private String teamName;
    private String pathToProPic;

    private Long userIdOriginal;

    private int position;

    private double points;

    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "leaderboard_id")
    private Leaderboard leaderboard;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFulleName() {
        return fulleName;
    }

    public void setFulleName(String fulleName) {
        this.fulleName = fulleName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getUserIdOriginal() {
        return userIdOriginal;
    }

    public void setUserIdOriginal(Long userIdOriginal) {
        this.userIdOriginal = userIdOriginal;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {


        Formatter formatter = new Formatter();
        formatter.format("%.2f", points);
        this.points = Double.parseDouble(formatter.toString());
    }

    public Leaderboard getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
    }

    @Override
    public int compareTo(Leaderelement o) {
        return this.getPosition() - o.getPosition();
//        return Double.compare(this.getPoints(), (o.getPoints()));
    }

    @Override
    public String toString() {
        return "Leaderelement{" +
                "id=" + id +
                ", fulleName='" + fulleName + '\'' +
                ", teamName='" + teamName + '\'' +
                ", pathToProPic='" + pathToProPic + '\'' +
                ", userIdOriginal=" + userIdOriginal +
                ", position=" + position +
                ", points=" + points +
                ", leaderboard=" + leaderboard +
                '}';
    }

    public String getPathToProPic() {
        return pathToProPic;
    }

    public void setPathToProPic(String pathToProPic) {
        this.pathToProPic = pathToProPic;
    }
}
