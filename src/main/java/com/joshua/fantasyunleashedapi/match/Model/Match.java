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
@Table(name = "matches")
public class Match {
    @Id
    @Column(name="match_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer matchId;
    @OneToOne
    @JoinColumn(name="home_team_id")
    private Team homeTeam;
    @OneToOne
    @JoinColumn(name="away_team_id")
    private Team awayTeam;
    private Integer week;
    @Column(name="home_score")
    private Integer homeScore;
    @Column(name="away_score")
    private Integer awayScore;
    private Boolean completed;
}
