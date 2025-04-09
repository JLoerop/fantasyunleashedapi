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

    @PostMapping("/creatematches")
    public List<Match> createMatches(@RequestParam Integer leagueId){
        return matchService.createMatches(leagueId);
    }

    @PutMapping("/calculatescores")
    public List<Match> calculateMatches(@RequestParam Integer leagueId, @RequestParam Integer week){
        return matchService.calculateMatches(leagueId, week);
    }
    @GetMapping("/getmatchbyid")
    public Match getMatchById(@RequestParam Integer matchId){
        return matchService.getMatchById(matchId);
    }
}
