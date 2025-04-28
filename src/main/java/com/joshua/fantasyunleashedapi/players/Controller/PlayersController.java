package com.joshua.fantasyunleashedapi.players.Controller;

import com.joshua.fantasyunleashedapi.league.Model.League;
import com.joshua.fantasyunleashedapi.league_player_points.Model.League_Player_Points;
import com.joshua.fantasyunleashedapi.match.Model.Match;
import com.joshua.fantasyunleashedapi.player_stats.Model.Player_Stats;
import com.joshua.fantasyunleashedapi.players.Model.Players;
import com.joshua.fantasyunleashedapi.players.Request.ChangePlayerRequest;
import com.joshua.fantasyunleashedapi.players.Request.PlacePlayerRequest;
import com.joshua.fantasyunleashedapi.players.Service.PlayersService;
import com.joshua.fantasyunleashedapi.rosters.Model.Rosters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PlayersController {
    @Autowired
    private PlayersService playersService;

    // request to get all of the players from the 3rd party api
    @GetMapping("/getplayers")
    public void getPlayers(){
        playersService.fetchAndSavePlayers();
    }

    // request that gets the list of all of the players in a league that have not been places on a team
    @GetMapping("/getplayerlist")
    public List<Players> getPlayerList(@RequestParam Integer leagueId){
        return playersService.getPlayerList(leagueId);
    }

    // request that gets the list of all of the players of a league that have been placed on a team
    @GetMapping("/getplayersplaced")
    public List<Players> getPlayersPlaced(@RequestParam Integer leagueId){
        return playersService.getPlayersPlaced(leagueId);
    }

    // request that places a player to a certain roster
    @PutMapping("/placeplayer")
    public Players placePlayer(@RequestBody PlacePlayerRequest placePlayerRequest){
        return playersService.placePlayer(placePlayerRequest);
    }

    // request that will remove a player off of a certain roster
    @DeleteMapping("/deleteplayer")
    public Players deletePlayer(@RequestBody PlacePlayerRequest placePlayerRequest){
        return playersService.deletePlayer(placePlayerRequest);
    }

    // request that gets all of the players stats from the 3rd party api
    @GetMapping("/getplayerstats")
    public void getPlayerStats(){
        playersService.fetchAndSaveStats();
    }

    // request that gets all of the player stats that have been saved to the database
    @GetMapping("/getstats")
    public List<Player_Stats> getStats(){
        return playersService.getStats();
    }

    // request that gets all of the defenses stats from the 3rd party api
    @GetMapping("/getdefstats")
    public void getDefStats(){
        playersService.fetchAndSaveDefenseStats();
    }

    // request that calculates all of the player points based on each leagues custom settings
    @GetMapping("/calculateplayerpoints")
    public void calculatePlayerPoints(){
        playersService.calculateAndSaveLeaguePlayerPoints();
    }

    // request that takes two players and switches their positions on a roster
    @PutMapping("/changeposition")
    public Rosters changePosition(@RequestBody ChangePlayerRequest changePlayerRequest){
        return playersService.changePosition(changePlayerRequest);
    }

    // request that gets all of the players scores from the database by the specific team id
    @GetMapping("/getplayerscores")
    public List<League_Player_Points> getPlayerScores(@RequestParam Integer teamId){
        return playersService.getPlayerScores(teamId);
    }
}
