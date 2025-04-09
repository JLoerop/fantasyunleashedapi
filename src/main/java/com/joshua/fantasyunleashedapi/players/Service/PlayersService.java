package com.joshua.fantasyunleashedapi.players.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joshua.fantasyunleashedapi.league.Model.League;
import com.joshua.fantasyunleashedapi.league.Repository.LeagueRepository;
import com.joshua.fantasyunleashedapi.league.Service.LeagueService;
import com.joshua.fantasyunleashedapi.league_player_points.Model.League_Player_Points;
import com.joshua.fantasyunleashedapi.league_player_points.Repository.League_PlayerPointsRepository;
import com.joshua.fantasyunleashedapi.league_users.Model.League_Users;
import com.joshua.fantasyunleashedapi.league_users.Repository.League_UserRepository;
import com.joshua.fantasyunleashedapi.player_stats.Model.Player_Stats;
import com.joshua.fantasyunleashedapi.player_stats.Repository.Player_StatsRepository;
import com.joshua.fantasyunleashedapi.players.Model.Players;
import com.joshua.fantasyunleashedapi.players.Repository.PlayersRepository;
import com.joshua.fantasyunleashedapi.players.Request.ChangePlayerRequest;
import com.joshua.fantasyunleashedapi.players.Request.PlacePlayerRequest;
import com.joshua.fantasyunleashedapi.rosters.Model.Rosters;
import com.joshua.fantasyunleashedapi.rosters.Repository.RostersRepository;
import com.joshua.fantasyunleashedapi.settings.Model.Settings;
import com.joshua.fantasyunleashedapi.team.Model.Team;
import com.joshua.fantasyunleashedapi.team.Repository.TeamRepository;
import com.joshua.fantasyunleashedapi.utils.PerformanceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
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
    @Autowired
    private Player_StatsRepository playerStatsRepository;
    @Autowired
    private LeagueService leagueService;
    @Autowired
    private LeagueRepository leagueRepository;
    @Autowired
    private League_PlayerPointsRepository leaguePlayerPointsRepository;

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
                if (roster.getTe() != null) placedPlayers.add(roster.getTe());
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
            System.out.println("Hello");
            Team team = teamRepository.findById(placePlayerRequest.getTeamId()).orElseThrow(() -> new RuntimeException("Team not found"));
            Rosters roster = team.getRosters();
            if (isPlayerOnRoster(roster, playersRepository.findById(placePlayerRequest.getPlayerId()).orElseThrow(() -> new RuntimeException("Player not found")))) {
                throw new RuntimeException("Player is already on this team's roster.");
            }
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
                } else if (roster.getFlex() == null){
                    roster.setFlex(player);
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
                } else if (roster.getFlex() == null){
                    roster.setFlex(player);
                    placed = true;
                }
                break;
            case "TE":
                if (roster.getTe() == null) {
                    roster.setTe(player);
                    placed = true;
                }  else if (roster.getFlex() == null){
                    roster.setFlex(player);
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

    public Players deletePlayer(PlacePlayerRequest placePlayerRequest) {
        Instant startTime = PerformanceUtil.start();
        Players player = playersRepository.findById(placePlayerRequest.getPlayerId()).orElseThrow(() -> new RuntimeException("Player not found"));
        rostersRepository.removePlayerFromRoster(placePlayerRequest.getPlayerId());
        PerformanceUtil.stop(startTime);
        return player;
    }

    private boolean isPlayerOnRoster(Rosters roster, Players player) {
        return player.equals(roster.getQb()) ||
                player.equals(roster.getRb1()) ||
                player.equals(roster.getRb2()) ||
                player.equals(roster.getWr1()) ||
                player.equals(roster.getWr2()) ||
                player.equals(roster.getTe()) ||
                player.equals(roster.getKicker()) ||
                player.equals(roster.getDefense()) ||
                player.equals(roster.getFlex()) ||
                player.equals(roster.getBench1()) ||
                player.equals(roster.getBench2()) ||
                player.equals(roster.getBench3()) ||
                player.equals(roster.getBench4());
    }

    public void fetchAndSaveStats() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.sportsdata.io/v3/nfl/stats/json/PlayerGameStatsByWeek/2024/17?key=11704b18346f4d50bb168d95e8019c44";

        String jsonResponse = restTemplate.getForObject(url, String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            for (JsonNode node : rootNode) {
                String playerName = node.get("Name").asText();

                playersRepository.findByName(playerName).ifPresent(player -> {
                    Player_Stats stats = new Player_Stats();
                    stats.setPlayer(playersRepository.findById(player.getPlayerId()).orElseThrow(() -> new RuntimeException("Player not found")));
                    stats.setWeek(node.get("Week").asInt());
                    stats.setPassingYards((int) node.get("PassingYards").asDouble());
                    stats.setRushingYards((int) node.get("RushingYards").asDouble());
                    stats.setRushingTouchdowns((int) node.get("RushingTouchdowns").asDouble());
                    stats.setPassingTouchdowns((int) node.get("PassingTouchdowns").asDouble());
                    stats.setInterceptions((int) node.get("PassingInterceptions").asDouble());
                    stats.setFumbles((int) node.get("Fumbles").asDouble());
                    stats.setReceivingTouchdowns((int) node.get("ReceivingTouchdowns").asDouble());
                    stats.setReceivingYards((int) node.get("ReceivingYards").asDouble());
                    stats.setKickReturnTd((int) (node.get("KickReturnTouchdowns").asDouble() + node.get("PuntReturnTouchdowns").asDouble()));
                    stats.setTackles((int) (node.get("SoloTackles").asDouble() + node.get("AssistedTackles").asDouble()));
                    stats.setDefFantasyPoints(null);

                    playerStatsRepository.save(stats);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        }

    public List<Player_Stats> getStats(){
        List<Player_Stats> playerStats = playerStatsRepository.findAll();
        return playerStats;
    }

    public void fetchAndSaveDefenseStats() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.sportsdata.io/v3/nfl/stats/json/FantasyDefenseByGame/2024/17?key=11704b18346f4d50bb168d95e8019c44";

        String jsonResponse = restTemplate.getForObject(url, String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            for (JsonNode node : rootNode) {
                String playerName = node.get("Team").asText();

                playersRepository.findByTeamAndPosition(playerName, "DEF").ifPresent(player -> {
                    Player_Stats stats = new Player_Stats();
                    stats.setPlayer(playersRepository.findById(player.getPlayerId()).orElseThrow(() -> new RuntimeException("Player not found")));
                    stats.setWeek(node.get("Week").asInt());
                    stats.setDefFantasyPoints((int) node.get("FantasyPoints").asDouble());

                    playerStatsRepository.save(stats);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void calculateAndSaveLeaguePlayerPoints() {
        List<League> leagues = leagueRepository.findAll();

        for (League league : leagues) {
            Settings settings = leagueService.getLeague(league.getLeagueId()).getSettings();
            List<Player_Stats> playerStatsList = getStats();

            for (Player_Stats stat : playerStatsList) {
                int points = 0;

                points += (stat.getPassingYards() != null ? stat.getPassingYards() : 0) / 25;
                points += (stat.getPassingTouchdowns() != null ? stat.getPassingTouchdowns() : 0) * 4;
                points -= (stat.getInterceptions() != null ? stat.getInterceptions() : 0) * 2;
                points += (stat.getRushingYards() != null ? stat.getRushingYards() : 0) / 10;
                points += (stat.getRushingTouchdowns() != null ? stat.getRushingTouchdowns() : 0) * 6;
                points += (stat.getReceivingYards() != null ? stat.getReceivingYards() : 0) / 10;
                points += (stat.getReceivingTouchdowns() != null ? stat.getReceivingTouchdowns() : 0) * 6;

                if (settings.getQbTackle() && "QB".equals(stat.getPlayer().getPosition())) {
                    points += (stat.getTackles() != null ? stat.getTackles() : 0) * settings.getQbTackleValue();
                }

                if (settings.getKickReturn() && (stat.getKickReturnTd() != null && stat.getKickReturnTd() > 0)) {
                    points += stat.getKickReturnTd() * settings.getKickReturnValue();
                }

                if (settings.getNonQbThrowTd() && !"QB".equals(stat.getPlayer().getPosition())) {
                    points += (stat.getPassingTouchdowns() != null ? stat.getPassingTouchdowns() : 0) * settings.getNonQbThrowTdValue();
                }

                if (settings.getQbReceivingTd() && "QB".equals(stat.getPlayer().getPosition())) {
                    points += (stat.getReceivingTouchdowns() != null ? stat.getReceivingTouchdowns() : 0) * settings.getQbReceivingTdValue();
                }

                if("DEF".equals(stat.getPlayer().getPosition())){
                    points += stat.getDefFantasyPoints();
                }

                League_Player_Points leaguePlayerPoints = new League_Player_Points();
                leaguePlayerPoints.setLeague(league);
                leaguePlayerPoints.setPlayer(stat.getPlayer());
                leaguePlayerPoints.setWeek(stat.getWeek());
                leaguePlayerPoints.setFantasyPoints(points);

                leaguePlayerPointsRepository.save(leaguePlayerPoints);
            }
        }
    }

    public Rosters changePosition(ChangePlayerRequest changePlayerRequest){
        Rosters roster = rostersRepository.findById(changePlayerRequest.getRosterId()).orElseThrow(() -> new RuntimeException("Roster not found"));
        Players player1 = playersRepository.findById(changePlayerRequest.getPlayerId()).orElseThrow(() -> new RuntimeException("Player 1 not found"));
        Players player2 = playersRepository.findById(changePlayerRequest.getPlayerId2()).orElseThrow(() -> new RuntimeException("Player 2 not found"));

        String position1 = null, position2 = null;

        if (roster.getQb() != null && roster.getQb().getPlayerId().equals(player1.getPlayerId())) position1 = "qb";
        if (roster.getRb1() != null && roster.getRb1().getPlayerId().equals(player1.getPlayerId())) position1 = "rb1";
        if (roster.getRb2() != null && roster.getRb2().getPlayerId().equals(player1.getPlayerId())) position1 = "rb2";
        if (roster.getWr1() != null && roster.getWr1().getPlayerId().equals(player1.getPlayerId())) position1 = "wr1";
        if (roster.getWr2() != null && roster.getWr2().getPlayerId().equals(player1.getPlayerId())) position1 = "wr2";
        if (roster.getTe() != null && roster.getTe().getPlayerId().equals(player1.getPlayerId())) position1 = "te";
        if (roster.getFlex() != null && roster.getFlex().getPlayerId().equals(player1.getPlayerId())) position1 = "flex";
        if (roster.getKicker() != null && roster.getKicker().getPlayerId().equals(player1.getPlayerId())) position1 = "kicker";
        if (roster.getDefense() != null && roster.getDefense().getPlayerId().equals(player1.getPlayerId())) position1 = "defense";
        if (roster.getBench1() != null && roster.getBench1().getPlayerId().equals(player1.getPlayerId())) position1 = "bench1";
        if (roster.getBench2() != null && roster.getBench2().getPlayerId().equals(player1.getPlayerId())) position1 = "bench2";
        if (roster.getBench3() != null && roster.getBench3().getPlayerId().equals(player1.getPlayerId())) position1 = "bench3";
        if (roster.getBench4() != null && roster.getBench4().getPlayerId().equals(player1.getPlayerId())) position1 = "bench4";

        if (roster.getQb() != null && roster.getQb().getPlayerId().equals(player2.getPlayerId())) position2 = "qb";
        if (roster.getRb1() != null && roster.getRb1().getPlayerId().equals(player2.getPlayerId())) position2 = "rb1";
        if (roster.getRb2() != null && roster.getRb2().getPlayerId().equals(player2.getPlayerId())) position2 = "rb2";
        if (roster.getWr1() != null && roster.getWr1().getPlayerId().equals(player2.getPlayerId())) position2 = "wr1";
        if (roster.getWr2() != null && roster.getWr2().getPlayerId().equals(player2.getPlayerId())) position2 = "wr2";
        if (roster.getTe() != null && roster.getTe().getPlayerId().equals(player2.getPlayerId())) position2 = "te";
        if (roster.getFlex() != null && roster.getFlex().getPlayerId().equals(player2.getPlayerId())) position2 = "flex";
        if (roster.getKicker() != null && roster.getKicker().getPlayerId().equals(player2.getPlayerId())) position2 = "kicker";
        if (roster.getDefense() != null && roster.getDefense().getPlayerId().equals(player2.getPlayerId())) position2 = "defense";
        if (roster.getBench1() != null && roster.getBench1().getPlayerId().equals(player2.getPlayerId())) position2 = "bench1";
        if (roster.getBench2() != null && roster.getBench2().getPlayerId().equals(player2.getPlayerId())) position2 = "bench2";
        if (roster.getBench3() != null && roster.getBench3().getPlayerId().equals(player2.getPlayerId())) position2 = "bench3";
        if (roster.getBench4() != null && roster.getBench4().getPlayerId().equals(player2.getPlayerId())) position2 = "bench4";

        if (position1 == null || position2 == null) {
            throw new RuntimeException("Player(s) not found in roster");
        }

        if (!isValidSwap(player1, player2, position1, position2)) {
            throw new RuntimeException("Invalid position swap");
        }

        setPlayer(roster, position1, player2);
        setPlayer(roster, position2, player1);

        return rostersRepository.save(roster);
    }

    public List<League_Player_Points> getPlayerScores(Integer teamId){
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found"));
        Rosters roster = team.getRosters();
        List<Players> players = new ArrayList<>();
        players.add(roster.getQb());
        players.add(roster.getRb1());
        players.add(roster.getRb2());
        players.add(roster.getWr1());
        players.add(roster.getWr2());
        players.add(roster.getFlex());
        players.add(roster.getTe());
        players.add(roster.getDefense());
        players.add(roster.getKicker());
        players.add(roster.getBench1());
        players.add(roster.getBench2());
        players.add(roster.getBench3());
        players.add(roster.getBench4());

        List<League_Player_Points> leaguePlayerPoints = new ArrayList<>();

        for (Players player : players) {
            League_Player_Points points = leaguePlayerPointsRepository.findByPlayerAndLeagueAndWeek(playersRepository.findById(player.getPlayerId()).orElseThrow(() -> new RuntimeException("Player not found")), leagueRepository.findById(team.getLeagueUser().getLeague().getLeagueId()).orElseThrow(() -> new RuntimeException("League not found")), 17);
            leaguePlayerPoints.add(points);
        }
        return leaguePlayerPoints;
    }

    private boolean isValidSwap(Players player1, Players player2, String position1, String position2) {
        return isAllowedPosition(player1, position2) && isAllowedPosition(player2, position1);
    }

    private boolean isAllowedPosition(Players player, String position) {
        String pos = player.getPosition();
        if (position.startsWith("bench")) return true;
        if (position.equals("flex")) return pos.equals("RB") || pos.equals("WR");
        if (position.equals("rb1") || position.equals("rb2")) return pos.equals("RB");
        if (position.equals("wr1") || position.equals("wr2")) return pos.equals("WR");
        return position.equalsIgnoreCase(pos);
    }

    private void setPlayer(Rosters roster, String position, Players player) {
        switch (position) {
            case "qb" -> roster.setQb(player);
            case "rb1" -> roster.setRb1(player);
            case "rb2" -> roster.setRb2(player);
            case "wr1" -> roster.setWr1(player);
            case "wr2" -> roster.setWr2(player);
            case "te" -> roster.setTe(player);
            case "flex" -> roster.setFlex(player);
            case "kicker" -> roster.setKicker(player);
            case "defense" -> roster.setDefense(player);
            case "bench1" -> roster.setBench1(player);
            case "bench2" -> roster.setBench2(player);
            case "bench3" -> roster.setBench3(player);
            case "bench4" -> roster.setBench4(player);
        }
    }
}
