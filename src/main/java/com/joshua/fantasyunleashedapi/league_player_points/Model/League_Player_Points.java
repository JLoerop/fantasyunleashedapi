package com.joshua.fantasyunleashedapi.league_player_points.Model;

import com.joshua.fantasyunleashedapi.league.Model.League;
import com.joshua.fantasyunleashedapi.players.Model.Players;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class League_Player_Points {
    @Id
    @Column(name="league_player_points_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer leaguePlayerPointsId;
    @ManyToOne
    @JoinColumn(name="league_id")
    private League league;
    @OneToOne
    @JoinColumn(name="player_id")
    private Players player;
    private Integer week;
    @Column(name="fantasy_points")
    private Integer fantasyPoints;
}
