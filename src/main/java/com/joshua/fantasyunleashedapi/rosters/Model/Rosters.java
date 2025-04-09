package com.joshua.fantasyunleashedapi.rosters.Model;

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

public class Rosters {
    @Id
    @Column(name = "roster_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rosterId;

    @ManyToOne
    @JoinColumn(name = "qb_player_id")
    private Players qb;

    @ManyToOne
    @JoinColumn(name = "rb1_player_id")
    private Players rb1;

    @ManyToOne
    @JoinColumn(name = "rb2_player_id")
    private Players rb2;

    @ManyToOne
    @JoinColumn(name = "wr1_player_id")
    private Players wr1;

    @ManyToOne
    @JoinColumn(name = "wr2_player_id")
    private Players wr2;

    @ManyToOne
    @JoinColumn(name = "te_player_id")
    private Players te;

    @ManyToOne
    @JoinColumn(name = "flex_player_id")
    private Players flex;

    @ManyToOne
    @JoinColumn(name = "kicker_player_id")
    private Players kicker;

    @ManyToOne
    @JoinColumn(name = "def_player_id")
    private Players defense;

    @ManyToOne
    @JoinColumn(name = "bench1")
    private Players bench1;

    @ManyToOne
    @JoinColumn(name = "bench2")
    private Players bench2;

    @ManyToOne
    @JoinColumn(name = "bench3")
    private Players bench3;

    @ManyToOne
    @JoinColumn(name = "bench4")
    private Players bench4;
}
