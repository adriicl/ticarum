package com.ticarumproject.ticarum.dto;

import java.time.LocalDate;
import java.util.Set;

public class CreateCompetitionRequest {
    public String name;
    public String sport;
    public LocalDate startDate;
    public LocalDate endDate;
    public int pistas;
    public Set<LocalDate> reservedDays;
}
