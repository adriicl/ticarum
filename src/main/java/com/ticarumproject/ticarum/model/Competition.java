package com.ticarumproject.ticarum.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String sport;
    private LocalDate startDate;
    private LocalDate endDate;
    private int pistas;

    @ElementCollection
    @CollectionTable(name = "competition_reserved_days", joinColumns = @JoinColumn(name = "competition_id"))
    @Column(name = "reserved_day")
    private Set<LocalDate> reservedDays = new TreeSet<>();

   

    public Competition() {}

    public Competition(String name, String sport, LocalDate startDate, LocalDate endDate, int pistas, Set<LocalDate> reservedDays) {
        this.name = name;
        this.sport = sport;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pistas = pistas;
        this.reservedDays = reservedDays != null ? new TreeSet<>(reservedDays) : new TreeSet<>();
    }

    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSport() { return sport; }
    public void setSport(String sport) { this.sport = sport; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public int getPistas() { return pistas; }
    public void setPistas(int pistas) { this.pistas = pistas; }
    public Set<LocalDate> getReservedDays() { return reservedDays; }
    public void setReservedDays(Set<LocalDate> reservedDays) { this.reservedDays = reservedDays; }
}
