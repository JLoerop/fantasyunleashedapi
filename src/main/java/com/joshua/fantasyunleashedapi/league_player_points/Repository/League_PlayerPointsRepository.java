package com.joshua.fantasyunleashedapi.league_player_points.Repository;

import com.joshua.fantasyunleashedapi.league.Model.League;
import com.joshua.fantasyunleashedapi.league_player_points.Model.League_Player_Points;
import com.joshua.fantasyunleashedapi.players.Model.Players;
import org.springframework.data.jpa.repository.JpaRepository;

public interface League_PlayerPointsRepository extends JpaRepository<League_Player_Points, Integer> {
    League_Player_Points findByPlayerAndLeagueAndWeek(Players player, League league, Integer week);
}
