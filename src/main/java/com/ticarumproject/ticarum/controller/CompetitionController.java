package com.ticarumproject.ticarum.controller;

import com.ticarumproject.ticarum.dto.*;
import com.ticarumproject.ticarum.model.Competition;
import com.ticarumproject.ticarum.model.Team;
import com.ticarumproject.ticarum.service.CompetitionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/competitions")
public class CompetitionController {

    private final CompetitionService service;

    public CompetitionController(CompetitionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CompetitionDto> createCompetition(@Valid @RequestBody CreateCompetitionRequest req) {
        Competition c = service.createCompetition(req);
        CompetitionDto dto = new CompetitionDto();
        dto.id = c.getId();
        dto.name = c.getName();
        dto.sport = c.getSport();
        dto.startDate = c.getStartDate();
        dto.endDate = c.getEndDate();
        dto.pistas = c.getPistas();
        dto.reservedDays = c.getReservedDays();
        return ResponseEntity.created(URI.create("/api/competitions/" + c.getId())).body(dto);
    }

    @PostMapping("/{competitionId}/teams")
    public ResponseEntity<String> registerTeam(@PathVariable Long competitionId, @Valid @RequestBody CreateTeamRequest req) {
        Team t = service.registerTeam(competitionId, req);
        return ResponseEntity.created(URI.create("/api/competitions/" + competitionId + "/teams/" + t.getId()))
                .body("Equipo registrado: " + t.getName());
    }

    @GetMapping("/{competitionId}/teams")
    public ResponseEntity<List<String>> getTeams(@PathVariable Long competitionId) {
        return ResponseEntity.ok(service.getTeamsByCompetition(competitionId));
    }

    @PostMapping("/{competitionId}/firstday/generate")
    public ResponseEntity<MatchesResponse> generateFirstDay(@PathVariable Long competitionId) {
        MatchesResponse res = service.generateFirstDayMatches(competitionId);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{competitionId}/firstday/matches")
    public ResponseEntity<List<MatchDto>> getMatches(@PathVariable Long competitionId) {
        return ResponseEntity.ok(service.getMatches(competitionId));
    }

    @GetMapping("/{competitionId}/firstday/unassigned")
    public ResponseEntity<List<String>> getUnassigned(@PathVariable Long competitionId) {
        return ResponseEntity.ok(service.getUnassignedTeams(competitionId));
    }
}
