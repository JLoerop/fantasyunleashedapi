package com.joshua.fantasyunleashedapi.players.Controller;

import com.joshua.fantasyunleashedapi.league.Model.League;
import com.joshua.fantasyunleashedapi.players.Model.Players;
import com.joshua.fantasyunleashedapi.players.Request.PlacePlayerRequest;
import com.joshua.fantasyunleashedapi.players.Service.PlayersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PlayersController {
    @Autowired
    private PlayersService playersService;

    @GetMapping("/getplayers")
    public void getLeague(){
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
}
