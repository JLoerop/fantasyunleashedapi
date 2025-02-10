package com.joshua.fantasyunleashedapi.player_stats.Model;

import com.joshua.fantasyunleashedapi.players.Model.Players;
import com.joshua.fantasyunleashedapi.team.Model.Team;
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
    @Column(name="stat_id")
    private Integer statId;
    @ManyToOne
    @JoinColumn(name="player_id")
    private Players player;
    private Integer week;
    @Column(name="stat_type")
    private String statType;
    @Column(name="stat_value")
    private Integer statValue;
}
