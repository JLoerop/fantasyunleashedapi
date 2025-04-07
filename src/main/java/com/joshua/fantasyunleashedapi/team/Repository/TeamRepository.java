package com.joshua.fantasyunleashedapi.team.Repository;

import com.joshua.fantasyunleashedapi.players.Model.Players;
import com.joshua.fantasyunleashedapi.team.Model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    @Query("SELECT t FROM Team t WHERE t.leagueUser.leagueUserId = :leagueUserId")
    Team findByLeagueUserId(Integer leagueUserId);
}
