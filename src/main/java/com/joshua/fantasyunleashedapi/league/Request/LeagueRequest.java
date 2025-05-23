package com.joshua.fantasyunleashedapi.league.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LeagueRequest {
    private String leagueName;
    private Integer teams;
    private Integer userId;
    private Integer qbTackleValue;
    private Integer kickReturnValue;
    private Integer nonQbThrowTdValue;
    private Integer qbReceivingTdValue;
}
