package com.joshua.fantasyunleashedapi.players.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joshua.fantasyunleashedapi.league_users.Model.League_Users;
import com.joshua.fantasyunleashedapi.league_users.Repository.League_UserRepository;
import com.joshua.fantasyunleashedapi.players.Model.Players;
import com.joshua.fantasyunleashedapi.players.Repository.PlayersRepository;
import com.joshua.fantasyunleashedapi.players.Request.PlacePlayerRequest;
import com.joshua.fantasyunleashedapi.rosters.Model.Rosters;
import com.joshua.fantasyunleashedapi.rosters.Repository.RostersRepository;
import com.joshua.fantasyunleashedapi.team.Model.Team;
import com.joshua.fantasyunleashedapi.team.Repository.TeamRepository;
import com.joshua.fantasyunleashedapi.utils.PerformanceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayersService {
    @Autowired
    private PlayersRepository playersRepository;
    @Autowired
    private League_UserRepository leagueUserRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private RostersRepository rostersRepository;

    public void fetchAndSavePlayers() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.sportsdata.io/v3/nfl/stats/json/FantasyPlayers?key=11704b18346f4d50bb168d95e8019c44";

        String jsonResponse = restTemplate.getForObject(url, String.class);

        if (jsonResponse != null) {
            savePlayersFromJson(jsonResponse);
        }
    }

    public void savePlayersFromJson(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            for (JsonNode node : rootNode) {
                Players player = new Players();
                player.setPlayerId(node.get("PlayerID").asInt());
                player.setName(node.get("Name").asText());
                player.setTeam(node.get("Team").asText());
                player.setPosition(node.get("Position").asText());

                playersRepository.save(player);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Players> getPlayerList(Integer leagueId){
        Instant startTime = PerformanceUtil.start();
        List<Players> players;
        players = playersRepository.findAll();
        List<Players> placedPlayers = getPlayersPlaced(leagueId);
        players.removeAll(placedPlayers);
        PerformanceUtil.stop(startTime);
        return players;
    }

    public List<Players> getPlayersPlaced(Integer leagueId) {
        Instant startTime = PerformanceUtil.start();
        List<League_Users> leagueUsers = leagueUserRepository.findByLeagueId(leagueId);
        List<Team> teams = new ArrayList<>();
        for (League_Users leagueUser : leagueUsers) {
            Team team = teamRepository.findByLeagueUserId(leagueUser.getLeagueUserId());
            teams.add(team);
        }
        List<Players> placedPlayers = new ArrayList<>();

        for (Team team : teams) {
            Rosters roster = team.getRosters();

            if (roster != null) {
                if (roster.getQb() != null) placedPlayers.add(roster.getQb());
                if (roster.getRb1() != null) placedPlayers.add(roster.getRb1());
                if (roster.getRb2() != null) placedPlayers.add(roster.getRb2());
                if (roster.getWr1() != null) placedPlayers.add(roster.getWr1());
                if (roster.getWr2() != null) placedPlayers.add(roster.getWr2());
                if (roster.getFlex() != null) placedPlayers.add(roster.getFlex());
                if (roster.getKicker() != null) placedPlayers.add(roster.getKicker());
                if (roster.getDefense() != null) placedPlayers.add(roster.getDefense());
                if (roster.getBench1() != null) placedPlayers.add(roster.getBench1());
                if (roster.getBench2() != null) placedPlayers.add(roster.getBench2());
                if (roster.getBench3() != null) placedPlayers.add(roster.getBench3());
                if (roster.getBench4() != null) placedPlayers.add(roster.getBench4());
            }
        }
        PerformanceUtil.stop(startTime);
        return placedPlayers;
    }
    public Players placePlayer(PlacePlayerRequest placePlayerRequest){
        Instant startTime = PerformanceUtil.start();

        Optional<Rosters> onRoster = rostersRepository.findRosterByPlayerId(placePlayerRequest.getPlayerId());
        if (onRoster.isPresent()) {
            rostersRepository.removePlayerFromRoster(placePlayerRequest.getPlayerId());
        }

        Players player = playersRepository.findById(placePlayerRequest.getPlayerId()).orElseThrow(() -> new RuntimeException("Player not found"));

        Team team = teamRepository.findById(placePlayerRequest.getTeamId()).orElseThrow(() -> new RuntimeException("Team not found"));

        Rosters roster = team.getRosters();

        String position = player.getPosition();
        boolean placed = false;

        switch (position.toUpperCase()) {
            case "QB":
                if (roster.getQb() == null) {
                    roster.setQb(player);
                    placed = true;
                }
                break;
            case "RB":
                if (roster.getRb1() == null) {
                    roster.setRb1(player);
                    placed = true;
                } else if (roster.getRb2() == null) {
                    roster.setRb2(player);
                    placed = true;
                }
                break;
            case "WR":
                if (roster.getWr1() == null) {
                    roster.setWr1(player);
                    placed = true;
                } else if (roster.getWr2() == null) {
                    roster.setWr2(player);
                    placed = true;
                }
                break;
            case "K":
                if (roster.getKicker() == null) {
                    roster.setKicker(player);
                    placed = true;
                }
                break;
            case "DEF":
                if (roster.getDefense() == null) {
                    roster.setDefense(player);
                    placed = true;
                }
                break;
            default:
                if (roster.getFlex() == null) {
                    roster.setFlex(player);
                    placed = true;
                }
        }

        if (!placed) {
            if (roster.getBench1() == null) {
                roster.setBench1(player);
            } else if (roster.getBench2() == null) {
                roster.setBench2(player);
            } else if (roster.getBench3() == null) {
                roster.setBench3(player);
            } else if (roster.getBench4() == null) {
                roster.setBench4(player);
            } else {
                throw new RuntimeException("All roster positions and bench spots are full.");
            }
        }

        rostersRepository.save(roster);
        PerformanceUtil.stop(startTime);
        return player;
    }
}
