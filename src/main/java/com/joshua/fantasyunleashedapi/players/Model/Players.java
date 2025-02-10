package com.joshua.fantasyunleashedapi.players.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Players {
    @Id
    @Column(name = "player_id")
    private Integer playerId;
    private String position;
    private String name;
    private String team;
}
