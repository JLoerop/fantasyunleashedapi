package com.joshua.fantasyunleashedapi.players.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlacePlayerRequest {
    private Integer playerId;
    private Integer teamId;
}
