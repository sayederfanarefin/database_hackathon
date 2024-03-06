package com.databasecourse.erfan.persistence.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Leaderboard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "leaderboard", cascade= CascadeType.ALL)
    private List<Leaderelement> leaderelements = new ArrayList<>();

    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "hackathon_id")
    private Hackathon hackathon;

    public Hackathon getHackathon() {
        return hackathon;
    }

    public void setHackathon(Hackathon hackathon) {
        this.hackathon = hackathon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Leaderelement> getLeaderelements() {
        return leaderelements;
    }

    public void setLeaderelements(List<Leaderelement> leaderelements) {
        this.leaderelements = leaderelements;
    }
}
