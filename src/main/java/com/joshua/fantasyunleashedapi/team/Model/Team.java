package com.joshua.fantasyunleashedapi.team.Model;

import com.joshua.fantasyunleashedapi.league_users.Model.League_Users;
import com.joshua.fantasyunleashedapi.players.Model.Players;
import com.joshua.fantasyunleashedapi.rosters.Model.Rosters;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Team {
    @Id
    @Column(name = "team_id")
    private Integer teamId;
    @Column(name = "team_name")
    private String teamName;
    @OneToOne
    @JoinColumn(name = "league_user_id")
    private League_Users leagueUser;
    @OneToMany
    @JoinColumn(name="roster_id")
    private List<Rosters> rosters;

}
