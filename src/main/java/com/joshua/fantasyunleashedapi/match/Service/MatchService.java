package com.joshua.fantasyunleashedapi.match.Service;

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

    public Match getMatchById(Integer matchId){
        Instant startTime = PerformanceUtil.start();
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new RuntimeException("Match not found"));
        PerformanceUtil.stop(startTime);
        return match;
    }
}
