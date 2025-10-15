package com.ticarumproject.ticarum.service;

import com.ticarumproject.ticarum.dto.*;
import com.ticarumproject.ticarum.model.*;
import com.ticarumproject.ticarum.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    public CompetitionService(CompetitionRepository competitionRepository,
                              TeamRepository teamRepository,
                              MatchRepository matchRepository) {
        this.competitionRepository = competitionRepository;
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

    public Competition createCompetition(CreateCompetitionRequest req) {
        Competition c = new Competition(req.name, req.sport, req.startDate, req.endDate, req.pistas, req.reservedDays);
        return competitionRepository.save(c);
    }

    public Team registerTeam(Long competitionId, CreateTeamRequest req) {
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new NoSuchElementException("Competición no encontrada: " + competitionId));
        teamRepository.findByNameAndCompetition(req.name, competition).ifPresent(t -> {
            throw new IllegalArgumentException("Equipo ya registrado en la competición");
        });
        Team team = new Team(req.name, competition);
        return teamRepository.save(team);
    }

    public List<String> getTeamsByCompetition(Long competitionId) {
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new NoSuchElementException("Competición no encontrada"));
        return teamRepository.findByCompetitionOrderById(competition).stream()
                .map(Team::getName).collect(Collectors.toList());
    }

    @Transactional
    public MatchesResponse generateFirstDayMatches(Long competitionId) {
        Competition comp = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new NoSuchElementException("Competición no encontrada"));

        // Borrar partidos previos de la primera jornada (si existieran)
        List<MatchEntity> existing = matchRepository.findByCompetitionOrderByDate(comp);
        if (!existing.isEmpty()) {
            matchRepository.deleteAll(existing);
        }

        List<Team> teams = teamRepository.findByCompetitionOrderById(comp);
        List<Team> teamList = new ArrayList<>(teams);

        MatchesResponse response = new MatchesResponse();
        response.assignedMatches = new ArrayList<>();
        response.unassignedTeams = new ArrayList<>();

        if (teamList.size() < 2) {
            response.unassignedTeams = teamList.stream().map(Team::getName).collect(Collectors.toList());
            return response;
        }

        List<LocalDate> reserved = new ArrayList<>(comp.getReservedDays());
        Collections.sort(reserved);

        if (reserved.isEmpty()) {
            reserved.add(comp.getStartDate() != null ? comp.getStartDate() : LocalDate.now());
        }

        int pistas = Math.max(0, comp.getPistas());
        if (pistas == 0) {
            response.unassignedTeams = teamList.stream().map(Team::getName).collect(Collectors.toList());
            return response;
        }

        int capacityPerDay = pistas * 2;

        int totalCapacityMatches = capacityPerDay * reserved.size();

        int neededMatches = teamList.size() / 2;

        int possibleMatches = Math.min(neededMatches, totalCapacityMatches);

        int pairingsToCreate = possibleMatches;

        List<MatchEntity> created = new ArrayList<>();
        int teamIndex = 0;
        int createdMatchesCount = 0;

        outer:
        for (LocalDate date : reserved) {
            for (int pista = 1; pista <= pistas; pista++) {
                for (int slot = 0; slot < 2; slot++) {
                    if (createdMatchesCount >= pairingsToCreate) break outer;
                    if (teamIndex + 1 >= teamList.size()) break outer;
                    Team a = teamList.get(teamIndex++);
                    Team b = teamList.get(teamIndex++);
                    MatchEntity match = new MatchEntity(comp, a, b, date, pista);
                    created.add(match);
                    createdMatchesCount++;
                }
            }
        }

        matchRepository.saveAll(created);

        response.assignedMatches = created.stream().map(m -> {
            MatchDto dto = new MatchDto();
            dto.id = m.getId();
            dto.teamA = m.getTeamA().getName();
            dto.teamB = m.getTeamB().getName();
            dto.date = m.getDate();
            dto.pista = m.getPistaNumber();
            return dto;
        }).collect(Collectors.toList());

        if (teamIndex < teamList.size()) {
            for (int i = teamIndex; i < teamList.size(); i++) {
                response.unassignedTeams.add(teamList.get(i).getName());
            }
        }

        return response;
    }

    public List<MatchDto> getMatches(Long competitionId) {
        Competition comp = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new NoSuchElementException("Competición no encontrada"));
        return matchRepository.findByCompetitionOrderByDate(comp).stream().map(m -> {
            MatchDto dto = new MatchDto();
            dto.id = m.getId();
            dto.teamA = m.getTeamA().getName();
            dto.teamB = m.getTeamB().getName();
            dto.date = m.getDate();
            dto.pista = m.getPistaNumber();
            return dto;
        }).collect(Collectors.toList());
    }

    public List<String> getUnassignedTeams(Long competitionId) {
        Competition comp = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new NoSuchElementException("Competición no encontrada"));
        List<Team> teams = teamRepository.findByCompetitionOrderById(comp);
        List<MatchEntity> matches = matchRepository.findByCompetitionOrderByDate(comp);
        Set<Long> assignedTeamIds = new HashSet<>();
        for (MatchEntity m : matches) {
            assignedTeamIds.add(m.getTeamA().getId());
            assignedTeamIds.add(m.getTeamB().getId());
        }
        return teams.stream()
                .filter(t -> !assignedTeamIds.contains(t.getId()))
                .map(Team::getName)
                .collect(Collectors.toList());
    }
}
