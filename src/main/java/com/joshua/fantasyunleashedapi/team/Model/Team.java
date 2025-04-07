package com.joshua.fantasyunleashedapi.team.Model;

import com.joshua.fantasyunleashedapi.league_users.Model.League_Users;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer teamId;
    @Column(name = "team_name")
    private String teamName;
    @OneToOne
    @JoinColumn(name = "league_user_id")
    private League_Users leagueUser;
    @OneToOne
    @JoinColumn(name="roster_id")
    private Rosters rosters;

}
