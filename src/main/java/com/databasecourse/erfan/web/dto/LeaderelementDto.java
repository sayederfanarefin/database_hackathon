package com.databasecourse.erfan.web.dto;

import com.databasecourse.erfan.persistence.model.Leaderboard;

import javax.persistence.*;
import java.util.Formatter;


public class LeaderelementDto  {

    private Long id;

    private String fulleName;

    private String teamName;
    private String pathToProPic;

    private Long userIdOriginal;

    private int position;

    private double points;

    public String getPathToProPic() {
        return pathToProPic;
    }

    public void setPathToProPic(String pathToProPic) {
        this.pathToProPic = pathToProPic;
    }

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
}
