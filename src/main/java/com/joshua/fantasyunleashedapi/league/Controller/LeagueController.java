package com.joshua.fantasyunleashedapi.league.Controller;

import com.joshua.fantasyunleashedapi.league.Model.League;
import com.joshua.fantasyunleashedapi.league.Request.LeagueRequest;
import com.joshua.fantasyunleashedapi.league.Request.LeagueUpdateRequest;
import com.joshua.fantasyunleashedapi.league.Service.LeagueService;
import com.joshua.fantasyunleashedapi.league_users.Model.League_Users;
import com.joshua.fantasyunleashedapi.match.Model.Match;
import com.joshua.fantasyunleashedapi.rosters.Model.Rosters;
import com.joshua.fantasyunleashedapi.team.Model.Team;
import com.joshua.fantasyunleashedapi.users.Model.User;
import com.joshua.fantasyunleashedapi.users.Request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LeagueController {
    @Autowired
    private LeagueService leagueService;

    // request to be able to create a league
    @PostMapping("/createleague")
    public League createLeague(@RequestBody LeagueRequest leagueRequest){
        return leagueService.createLeague(leagueRequest);
    }

    // request for getting a specific league
    @GetMapping("/getleague")
    public League getLeague(@RequestParam Integer leagueId){
        return leagueService.getLeague(leagueId);
    }

    //request for getting all the leagues a user is in
    @GetMapping("/getleaguesforuser")
    public List<League> getLeaguesForUser(@RequestParam Integer userId){
        return leagueService.getLeaguesForUser(userId);
    }

    //request for checking if a user is the commissioner of the league
    @GetMapping("/isusercommissioner")
    public Boolean isUserCommissioner(@RequestParam Integer userId, @RequestParam Integer leagueId){
        return leagueService.isUserCommissioner(userId, leagueId);
    }

    //request for editing a leagues settings
    @PutMapping("/editleague")
    public League editLeague(@RequestBody LeagueUpdateRequest leagueRequest){
        return leagueService.editLeague(leagueRequest);
    }

    //request to handle when someone joins the league through the link
    @PostMapping("/joinleague")
    public League_Users joinLeague(@RequestParam Integer leagueId, @RequestParam("email") String email){
        return leagueService.joinLeague(leagueId, email);
    }

    // request for creating a team for a user
    @PostMapping("/createteam")
    public Team createTeam(@RequestParam Integer leagueId, @RequestParam String teamName, @RequestParam Integer userId){
        return leagueService.createTeam(leagueId, teamName, userId);
    }

    // request to get a users team from the league id and user id
    @GetMapping("/getteam")
    public Team getTeam(@RequestParam Integer leagueId, @RequestParam Integer userId){
        return leagueService.getTeam(leagueId, userId);
    }

    // request to get all of teams in a league
    @GetMapping("/getteamsinleague")
    public List<Team> getTeamsInLeague(@RequestParam Integer leagueId){
        return leagueService.getTeamsInLeague(leagueId);
    }

    // request that validates a leagues size to verify if a user is able to join the league
    @GetMapping("/validateleaguesize")
    public Boolean validateLeagueSize(@RequestParam Integer leagueId){
        return leagueService.validateLeagueSize(leagueId);
    }

    // request to get all of the teams that a user has
    @GetMapping("/getteamsforuser")
    public List<Team> getTeamsForUser(@RequestParam Integer userId){
        return leagueService.getTeamsForUser(userId);
    }

    // request to get the matches that a user has that week
    @GetMapping("/getmatchesforuser")
    public List<Match> getMatchesForUser(@RequestParam Integer userId, @RequestParam Integer week){
        return leagueService.getMatchesForUser(userId, week);
    }

    // request to get a team by sending the team id
    @GetMapping("/getteambyteamid")
    public Team getTeamByTeamId(@RequestParam Integer teamId){
        return leagueService.getTeamByTeamId(teamId);
    }

    // request to get the matchup of that team for that weak
    @GetMapping("/getmatch")
    public Match getMatch(@RequestParam Integer teamId, @RequestParam Integer week){
        return leagueService.getMatch(teamId, week);
    }
}
