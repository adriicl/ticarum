package com.ticarumproject.ticarum.repository;

import com.ticarumproject.ticarum.model.Competition;
import com.ticarumproject.ticarum.model.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
    List<MatchEntity> findByCompetitionOrderByDate(Competition competition);
}