package com.joshua.fantasyunleashedapi.player_stats.Repository;

import com.joshua.fantasyunleashedapi.league.Model.League;
import com.joshua.fantasyunleashedapi.league_users.Model.League_Users;
import com.joshua.fantasyunleashedapi.player_stats.Model.Player_Stats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Player_StatsRepository extends JpaRepository<Player_Stats, Integer> {

}
