package com.joshua.fantasyunleashedapi.league.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LeagueUpdateRequest {
    private Integer leagueId;
    private Integer qbTackleValue;
    private Integer kickReturnValue;
    private Integer nonQbThrowTdValue;
    private Integer qbReceivingTdValue;
}
