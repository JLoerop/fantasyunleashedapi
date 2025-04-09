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

    @GetMapping("/getplayers")
    public void getPlayers(){
        playersService.fetchAndSavePlayers();
    }

    @GetMapping("/getplayerlist")
    public List<Players> getPlayerList(@RequestParam Integer leagueId){
        return playersService.getPlayerList(leagueId);
    }

    @GetMapping("/getplayersplaced")
    public List<Players> getPlayersPlaced(@RequestParam Integer leagueId){
        return playersService.getPlayersPlaced(leagueId);
    }

    @PutMapping("/placeplayer")
    public Players placePlayer(@RequestBody PlacePlayerRequest placePlayerRequest){
        return playersService.placePlayer(placePlayerRequest);
    }
    @DeleteMapping("/deleteplayer")
    public Players deletePlayer(@RequestBody PlacePlayerRequest placePlayerRequest){
        return playersService.deletePlayer(placePlayerRequest);
    }
    @GetMapping("/getplayerstats")
    public void getPlayerStats(){
        playersService.fetchAndSaveStats();
    }
    @GetMapping("/getstats")
    public List<Player_Stats> getStats(){
        return playersService.getStats();
    }
    @GetMapping("/getdefstats")
    public void getDefStats(){
        playersService.fetchAndSaveDefenseStats();
    }
    @GetMapping("/calculateplayerpoints")
    public void calculatePlayerPoints(){
        playersService.calculateAndSaveLeaguePlayerPoints();
    }
    @PutMapping("/changeposition")
    public Rosters changePosition(@RequestBody ChangePlayerRequest changePlayerRequest){
        return playersService.changePosition(changePlayerRequest);
    }
    @GetMapping("/getplayerscores")
    public List<League_Player_Points> getPlayerScores(@RequestParam Integer teamId){
        return playersService.getPlayerScores(teamId);
    }
}
