package com.taskdb.repository;

import com.taskdb.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    @Query("SELECT COUNT(t) FROM Team t WHERE t.name = ?1")
    long findTeamByName(String name);
}

