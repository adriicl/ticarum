package com.ticarumproject.ticarum.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateTeamRequest {
    @NotBlank
    public String name;
}
