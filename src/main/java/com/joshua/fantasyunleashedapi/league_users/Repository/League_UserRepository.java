package com.joshua.fantasyunleashedapi.league_users.Repository;

import com.joshua.fantasyunleashedapi.league.Model.League;
import com.joshua.fantasyunleashedapi.league_users.Model.League_Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface League_UserRepository extends JpaRepository<League_Users, Integer> {
    @Query("SELECT lu.league FROM League_Users lu WHERE lu.user.id = :userId")
    List<League> findLeaguesByUserId(@Param("userId") Integer userId);

    @Query("SELECT lu FROM League_Users lu WHERE lu.user.userId = :userId AND lu.league.leagueId = :leagueId")
    League_Users findByUserIdAndLeagueId(@Param("userId") Integer userId, @Param("leagueId") Integer leagueId);

    @Query(value = "SELECT * FROM league_users WHERE league_id = :leagueId", nativeQuery = true)
    List<League_Users> findByLeagueId(@Param("leagueId") Integer leagueId);
}
