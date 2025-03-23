package com.joshua.fantasyunleashedapi.league_users.Model;

import com.joshua.fantasyunleashedapi.league.Model.League;
import com.joshua.fantasyunleashedapi.users.Model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class League_Users {
    @Id
    @Column(name="league_user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer leagueUserId;
    private Boolean commissioner;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="league_id")
    private League league;
}
