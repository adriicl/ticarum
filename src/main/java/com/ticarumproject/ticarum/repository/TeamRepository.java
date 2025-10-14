package com.ticarumproject.ticarum.repository;

import com.ticarumproject.ticarum.model.Competition;
import com.ticarumproject.ticarum.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByCompetitionOrderById(Competition competition);
    Optional<Team> findByNameAndCompetition(String name, Competition competition);
}