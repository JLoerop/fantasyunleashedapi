package com.joshua.fantasyunleashedapi.match.Controller;

import com.joshua.fantasyunleashedapi.match.Model.Match;
import com.joshua.fantasyunleashedapi.match.Service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MatchController {
    @Autowired
    MatchService matchService;

    // request that creates all of the matches for a certain league
    @PostMapping("/creatematches")
    public List<Match> createMatches(@RequestParam Integer leagueId){
        return matchService.createMatches(leagueId);
    }

    // request that calculates all of the scores for a league and the week it is in
    @PutMapping("/calculatescores")
    public List<Match> calculateMatches(@RequestParam Integer leagueId, @RequestParam Integer week){
        return matchService.calculateMatches(leagueId, week);
    }

    // request that gets a matchup by that matches id
    @GetMapping("/getmatchbyid")
    public Match getMatchById(@RequestParam Integer matchId){
        return matchService.getMatchById(matchId);
    }

    @GetMapping("/getmatchesbyleagueid")
    public List<Match> getMatchesByLeagueId(@RequestParam Integer leagueId){
        return matchService.getMatchesByLeagueId(leagueId);
    }
}
