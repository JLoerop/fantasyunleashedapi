package com.joshua.fantasyunleashedapi.league.Model;

import com.joshua.fantasyunleashedapi.settings.Model.Settings;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class League {
    @Id
    @Column(name="league_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer leagueId;
    @Column(name="league_name")
    private String leagueName;
    @OneToOne
    @JoinColumn(name="settings_id")
    private Settings settings;
    private Integer teams;
}
