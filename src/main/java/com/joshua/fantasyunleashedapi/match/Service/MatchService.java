package com.joshua.fantasyunleashedapi.match.Service;

import com.joshua.fantasyunleashedapi.league.Model.League;
import com.joshua.fantasyunleashedapi.league.Repository.LeagueRepository;
import com.joshua.fantasyunleashedapi.league.Service.LeagueService;
import com.joshua.fantasyunleashedapi.league_player_points.Model.League_Player_Points;
import com.joshua.fantasyunleashedapi.league_player_points.Repository.League_PlayerPointsRepository;
import com.joshua.fantasyunleashedapi.match.Model.Match;
import com.joshua.fantasyunleashedapi.match.Repository.MatchRepository;
import com.joshua.fantasyunleashedapi.players.Model.Players;
import com.joshua.fantasyunleashedapi.players.Repository.PlayersRepository;
import com.joshua.fantasyunleashedapi.team.Model.Team;
import com.joshua.fantasyunleashedapi.utils.PerformanceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MatchService {
    @Autowired
    LeagueService leagueService;
    @Autowired
    MatchRepository matchRepository;
    @Autowired
    League_PlayerPointsRepository leaguePlayerPointsRepository;
    @Autowired
    LeagueRepository leagueRepository;
    @Autowired
    PlayersRepository playersRepository;

    // service that takes a league id and creates a random assortion of the teams in the league and assigns a matchup in that order
    // and then saves that match to the database
    public List<Match> createMatches(Integer leagueId){
        List<Team> teams = leagueService.getTeamsInLeague(leagueId);

        Collections.shuffle(teams);

        List<Match> matches = new ArrayList<>();

        for (int i = 0; i < teams.size(); i += 2) {
            Match match = new Match();
            match.setHomeTeam(teams.get(i));
            match.setAwayTeam(teams.get(i + 1));
            match.setWeek(17);
            match.setCompleted(false);

            matches.add(match);
        }
        matchRepository.saveAll(matches);
        return matches;
    }

    // service that will calculate the score of the matches for that league and the week it is in by getting the teams in the league
    // and then doing a for loop for all of the teams and calculating the score of the active players it then gets the match
    // and verifies if it is saving the home or away team and sets and saves the score
    public List<Match> calculateMatches(Integer leagueId, Integer week){
        List<Team> teams = leagueService.getTeamsInLeague(leagueId);

        List<Match> matches = new ArrayList<>();
        for (Team team : teams) {
            List<Players> players = getActivePlayers(team);
            Integer teamScore = calculateTeamScore(players, leagueId, week);

            Match match = matchRepository.findMatchByTeamAndWeek(team, week);

                if (match.getHomeTeam().equals(team)) {
                    match.setHomeScore(teamScore);
                } else if (match.getAwayTeam().equals(team)) {
                    match.setAwayScore(teamScore);
                }
                matches.add(match);
                matchRepository.save(match);
            }
        return matches;
    }

    // helper method to verify which players are on the bench by getting and then setting the positions that are not the bench
    private List<Players> getActivePlayers(Team team) {
        List<Players> activePlayers = new ArrayList<>();

        if (team.getRosters() != null) {
            if (team.getRosters().getQb() != null) activePlayers.add(team.getRosters().getQb());
            if (team.getRosters().getRb1() != null) activePlayers.add(team.getRosters().getRb1());
            if (team.getRosters().getRb2() != null) activePlayers.add(team.getRosters().getRb2());
            if (team.getRosters().getWr1() != null) activePlayers.add(team.getRosters().getWr1());
            if (team.getRosters().getWr2() != null) activePlayers.add(team.getRosters().getWr2());
            if (team.getRosters().getTe() != null) activePlayers.add(team.getRosters().getTe());
            if (team.getRosters().getFlex() != null) activePlayers.add(team.getRosters().getFlex());
            if (team.getRosters().getKicker() != null) activePlayers.add(team.getRosters().getKicker());
            if (team.getRosters().getDefense() != null) activePlayers.add(team.getRosters().getDefense());
        }

        return activePlayers;
    }

    // helper method that goes through a loop of all of the players brought in and getting their points for that week and league
    // from the database and then adding them up and then returning that score when the loop finishes
    private int calculateTeamScore(List<Players> players, Integer leagueId, Integer week) {
        int totalScore = 0;

        for (Players player : players) {
            League_Player_Points points = leaguePlayerPointsRepository.findByPlayerAndLeagueAndWeek(playersRepository.findById(player.getPlayerId()).orElseThrow(() -> new RuntimeException("Player not found")), leagueRepository.findById(leagueId).orElseThrow(() -> new RuntimeException("League not found")), week);
            if (points != null) {
                totalScore += points.getFantasyPoints();
            }
        }

        return totalScore;
    }

    // service that will get the specific match from the database using the match id
    public Match getMatchById(Integer matchId){
        Instant startTime = PerformanceUtil.start();
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new RuntimeException("Match not found"));
        PerformanceUtil.stop(startTime);
        return match;
    }

    public List<Match> getMatchesByLeagueId(Integer leagueId){
        Instant startTime = PerformanceUtil.start();
        List<Team> teams = leagueService.getTeamsInLeague(leagueId);
        List<Match> matches = new ArrayList<>();
        for (Team team : teams){
            Match match = matchRepository.findMatchByTeamAndWeek(team, 17);
            if(matches.contains(match)){
                System.out.println("Match already added continuing with rest.");
            }
            else{
                matches.add(match);
            }
        }
        PerformanceUtil.stop(startTime);
        return matches;
    }
}
