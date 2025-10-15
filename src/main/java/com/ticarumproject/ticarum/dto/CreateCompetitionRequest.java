package com.ticarumproject.ticarum.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

public class CreateCompetitionRequest {
    @NotBlank
    public String name;

    @NotBlank
    public String sport;

    @NotNull
    public LocalDate startDate;

    @NotNull
    public LocalDate endDate;

    @Min(1)
    public int pistas;

    // Puede ser vacío; si lo es, se usará startDate
    public Set<LocalDate> reservedDays;
}
