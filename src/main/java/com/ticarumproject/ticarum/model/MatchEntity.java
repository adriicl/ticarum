package com.ticarumproject.ticarum.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity(name = "Matches")
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Competition competition;

    @ManyToOne(optional = false)
    private Team teamA;

    @ManyToOne(optional = false)
    private Team teamB;

    private LocalDate date;

    private int pistaNumber;

    public MatchEntity() {}

    public MatchEntity(Competition competition, Team teamA, Team teamB, LocalDate date, int pistaNumber) {
        this.competition = competition;
        this.teamA = teamA;
        this.teamB = teamB;
        this.date = date;
        this.pistaNumber = pistaNumber;
    }

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Competition getCompetition() { return competition; }
    public void setCompetition(Competition competition) { this.competition = competition; }
    public Team getTeamA() { return teamA; }
    public void setTeamA(Team teamA) { this.teamA = teamA; }
    public Team getTeamB() { return teamB; }
    public void setTeamB(Team teamB) { this.teamB = teamB; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public int getPistaNumber() { return pistaNumber; }
    public void setPistaNumber(int pistaNumber) { this.pistaNumber = pistaNumber; }
}
