package com.joshua.fantasyunleashedapi.player_stats.Model;

import com.joshua.fantasyunleashedapi.players.Model.Players;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Player_Stats {
    @Id
    @Column(name = "stat_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer statId;
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Players player;
    private Integer week;
    @Column(name = "passing_yards")
    private Integer passingYards;
    @Column(name = "rushing_yards")
    private Integer rushingYards;
    @Column(name = "rushing_touchdowns")
    private Integer rushingTouchdowns;
    @Column(name = "passing_touchdowns")
    private Integer passingTouchdowns;
    private Integer interceptions;
    private Integer fumbles;
    @Column(name = "receiving_touchdowns")
    private Integer receivingTouchdowns;
    @Column(name = "receiving_yards")
    private Integer receivingYards;
    @Column(name = "kick_return_td")
    private Integer kickReturnTd;
    private Integer tackles;
    @Column(name = "def_fantasy_points")
    private Integer defFantasyPoints;

}
