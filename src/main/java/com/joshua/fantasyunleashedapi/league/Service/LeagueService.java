package com.joshua.fantasyunleashedapi.league.Service;

import com.joshua.fantasyunleashedapi.league.Model.League;
import com.joshua.fantasyunleashedapi.league.Repository.LeagueRepository;
import com.joshua.fantasyunleashedapi.league.Request.LeagueRequest;
import com.joshua.fantasyunleashedapi.league.Request.LeagueUpdateRequest;
import com.joshua.fantasyunleashedapi.league_users.Model.League_Users;
import com.joshua.fantasyunleashedapi.league_users.Repository.League_UserRepository;
import com.joshua.fantasyunleashedapi.rosters.Model.Rosters;
import com.joshua.fantasyunleashedapi.rosters.Repository.RostersRepository;
import com.joshua.fantasyunleashedapi.settings.Model.Settings;
import com.joshua.fantasyunleashedapi.settings.Repository.SettingsRepository;
import com.joshua.fantasyunleashedapi.team.Model.Team;
import com.joshua.fantasyunleashedapi.team.Repository.TeamRepository;
import com.joshua.fantasyunleashedapi.users.Model.User;
import com.joshua.fantasyunleashedapi.users.Repository.UserRepository;
import com.joshua.fantasyunleashedapi.utils.PerformanceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class LeagueService {
    @Autowired
    private LeagueRepository leagueRepository;
    @Autowired
    private SettingsRepository settingsRepository;
    @Autowired
    private League_UserRepository leagueUserRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private RostersRepository rostersRepository;

    // service that takes all of the league inputs and creates a league with all of the custom settings
    public League createLeague(LeagueRequest leagueRequest){
        Instant startTime = PerformanceUtil.start();

        Settings settings = new Settings();
        if(leagueRequest.getKickReturnValue() == 0){
            settings.setKickReturn(false);
        }
        else{
            settings.setKickReturn(true);
        }
        if(leagueRequest.getNonQbThrowTdValue() == 0){
            settings.setNonQbThrowTd(false);
        }
        else{
            settings.setNonQbThrowTd(true);
        }
        if(leagueRequest.getQbTackleValue() == 0){
            settings.setQbTackle(false);
        }
        else{
            settings.setQbTackle(true);
        }
        if(leagueRequest.getQbReceivingTdValue() == 0){
            settings.setQbReceivingTd(false);
        }
        else{
            settings.setQbReceivingTd(true);
        }
        settings.setKickReturnValue(leagueRequest.getKickReturnValue());
        settings.setQbReceivingTdValue(leagueRequest.getQbReceivingTdValue());
        settings.setNonQbThrowTdValue(leagueRequest.getNonQbThrowTdValue());
        settings.setQbTackleValue(leagueRequest.getQbTackleValue());
        settings = settingsRepository.save(settings);

        League league = new League();
        league.setLeagueName(leagueRequest.getLeagueName());
        league.setTeams(leagueRequest.getTeams());
        league.setSettings(settings);
        league = leagueRepository.save(league);

        User user = userRepository.findById(leagueRequest.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        League_Users leagueUser = new League_Users();
        leagueUser.setUser(user);
        leagueUser.setLeague(league);
        leagueUser.setCommissioner(true);

        leagueUserRepository.save(leagueUser);

        PerformanceUtil.stop(startTime);
        return league;
    }

    // service for returning a single league by league id
    public League getLeague(Integer leagueId){
        Instant startTime = PerformanceUtil.start();
        League league = leagueRepository.findById(leagueId).orElseThrow(() -> new RuntimeException("League not found"));
        PerformanceUtil.stop(startTime);
        return league;
    }

    // service that calls the repository to get all of the leagues a user is in
    public List<League> getLeaguesForUser(Integer userId){
        Instant startTime = PerformanceUtil.start();
        List<League> leagues = leagueUserRepository.findLeaguesByUserId(userId);
        PerformanceUtil.stop(startTime);
        return leagues;
    }

    // service that returns true or false based on if the user created the league
    public Boolean isUserCommissioner(Integer userId, Integer leagueId){
        Instant startTime = PerformanceUtil.start();
        League_Users leagueUser = leagueUserRepository.findByUserIdAndLeagueId(userId, leagueId);
        PerformanceUtil.stop(startTime);
        return leagueUser.getCommissioner();
    }

    // service that handles editing leagues by getting the existing settings and writing over the existing one
    public League editLeague(LeagueUpdateRequest leagueRequest){
        Instant startTime = PerformanceUtil.start();
        League league = leagueRepository.findById(leagueRequest.getLeagueId()).orElseThrow(() -> new RuntimeException("League not found"));
        Settings settings = settingsRepository.findById(league.getSettings().getSettingsId()).orElseThrow(() -> new RuntimeException("Settings not found"));
        settings.setQbTackleValue(leagueRequest.getQbTackleValue());
        settings.setKickReturnValue(leagueRequest.getKickReturnValue());
        settings.setNonQbThrowTdValue(leagueRequest.getNonQbThrowTdValue());
        settings.setQbReceivingTdValue(leagueRequest.getQbReceivingTdValue());
        settingsRepository.save(settings);
        PerformanceUtil.stop(startTime);
        return league;
    }

    // service to handle the request of joining a league which creates a new league user and saves to the database
    public League_Users joinLeague(Integer leagueId, String email, String password){
        Instant startTime = PerformanceUtil.start();
        List<Team> teams = getTeamsInLeague(leagueId);
        League league = leagueRepository.findById(leagueId).orElseThrow(() -> new RuntimeException("League not found"));
        if (teams.size() == league.getTeams()){
            throw new RuntimeException("This league is full. No more teams can be added.");
        }
        League_Users leagueUser = new League_Users();
        leagueUser.setCommissioner(false);
        leagueUser.setUser(userRepository.findByEmailAndPassword(email, password));
        leagueUser.setLeague(leagueRepository.findById(leagueId).orElseThrow(() -> new RuntimeException("League not found")));
        leagueUserRepository.save(leagueUser);
        PerformanceUtil.stop(startTime);
        return leagueUser;
    }

    public Team createTeam(Integer leagueId, String teamName, Integer userId){
        Instant startTime = PerformanceUtil.start();
        Team team = new Team();
        League_Users league_user = leagueUserRepository.findByUserIdAndLeagueId(userId, leagueId);
        team.setTeamName(teamName);
        team.setLeagueUser(league_user);
        Rosters roster = new Rosters();
        roster.setQb(null);
        roster.setRb1(null);
        roster.setRb2(null);
        roster.setWr1(null);
        roster.setWr2(null);
        roster.setFlex(null);
        roster.setKicker(null);
        roster.setDefense(null);
        roster.setBench1(null);
        roster.setBench2(null);
        roster.setBench3(null);
        roster.setBench4(null);

        roster = rostersRepository.save(roster);

        team.setRosters(roster);

        team = teamRepository.save(team);

        PerformanceUtil.stop(startTime);
        return team;
    }
    public Team getTeam(Integer leagueId, Integer userId){
        Instant startTime = PerformanceUtil.start();
        League_Users leagueUser = leagueUserRepository.findByUserIdAndLeagueId(userId, leagueId);
        Team team = teamRepository.findByLeagueUserId(leagueUser.getLeagueUserId());
        PerformanceUtil.stop(startTime);
        return team;
    }

    public List<Team> getTeamsInLeague(Integer leagueId){
        Instant startTime = PerformanceUtil.start();
        List<Team> teams = new ArrayList<>();
        List<League_Users> leagueUsers = leagueUserRepository.findByLeagueId(leagueId);

        for (League_Users leagueUser : leagueUsers) {
            Team team = teamRepository.findByLeagueUserId(leagueUser.getLeagueUserId());
            teams.add(team);
        }
        PerformanceUtil.stop(startTime);
        return teams;
    }

    public Boolean validateLeagueSize(Integer leagueId){
        Instant startTime = PerformanceUtil.start();
        Boolean isFull = false;
        List<Team> teams = getTeamsInLeague(leagueId);
        League league = leagueRepository.findById(leagueId).orElseThrow(() -> new RuntimeException("League not found"));
        if (teams.size() == league.getTeams()){
            isFull = true;
            return isFull;
        }
        PerformanceUtil.stop(startTime);
        return isFull;
    }
}
