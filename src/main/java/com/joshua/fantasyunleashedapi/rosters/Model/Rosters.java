package com.joshua.fantasyunleashedapi.rosters.Model;

import com.joshua.fantasyunleashedapi.players.Model.Players;
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
    private Integer rosterId;
    @ManyToOne
    @JoinColumn(name="player_id")
    private Players player;
}
