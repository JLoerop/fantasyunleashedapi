package com.joshua.fantasyunleashedapi.league.Controller;

import com.joshua.fantasyunleashedapi.league.Model.League;
import com.joshua.fantasyunleashedapi.league.Request.LeagueRequest;
import com.joshua.fantasyunleashedapi.league.Request.LeagueUpdateRequest;
import com.joshua.fantasyunleashedapi.league.Service.LeagueService;
import com.joshua.fantasyunleashedapi.league_users.Model.League_Users;
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
    public League_Users joinLeague(@RequestParam Integer leagueId, @RequestParam("email") String email, @RequestParam("password") String password){
        return leagueService.joinLeague(leagueId, email, password);
    }
}
