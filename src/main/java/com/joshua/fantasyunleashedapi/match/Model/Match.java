package com.joshua.fantasyunleashedapi.match.Model;

import com.joshua.fantasyunleashedapi.league.Model.League;
import com.joshua.fantasyunleashedapi.players.Model.Players;
import com.joshua.fantasyunleashedapi.team.Model.Team;
import com.joshua.fantasyunleashedapi.users.Model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Match {
    @Id
    @Column(name="match_id")
    private Integer matchId;
    @ManyToOne
    @JoinColumn(name="home_team_id")
    private Team homeTeam;
    @ManyToOne
    @JoinColumn(name="away_team_id")
    private Team awayTeam;
    private Integer week;
    @Column(name="home_score")
    private Integer homeScore;
    @Column(name="away_score")
    private Integer awayScore;
    private Boolean completed;
}
