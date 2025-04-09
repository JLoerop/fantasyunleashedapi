package com.joshua.fantasyunleashedapi.rosters.Repository;

import com.joshua.fantasyunleashedapi.rosters.Model.Rosters;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RostersRepository extends JpaRepository<Rosters, Integer> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE rosters r SET " +
            "qb_player_id = CASE WHEN qb_player_id = :playerId THEN NULL ELSE qb_player_id END, " +
            "rb1_player_id = CASE WHEN rb1_player_id = :playerId THEN NULL ELSE rb1_player_id END, " +
            "rb2_player_id = CASE WHEN rb2_player_id = :playerId THEN NULL ELSE rb2_player_id END, " +
            "wr1_player_id = CASE WHEN wr1_player_id = :playerId THEN NULL ELSE wr1_player_id END, " +
            "wr2_player_id = CASE WHEN wr2_player_id = :playerId THEN NULL ELSE wr2_player_id END, " +
            "te_player_id = CASE WHEN te_player_id = :playerId THEN NULL ELSE te_player_id END, " +
            "flex_player_id = CASE WHEN flex_player_id = :playerId THEN NULL ELSE flex_player_id END, " +
            "kicker_player_id = CASE WHEN kicker_player_id = :playerId THEN NULL ELSE kicker_player_id END, " +
            "def_player_id = CASE WHEN def_player_id = :playerId THEN NULL ELSE def_player_id END, " +
            "bench1 = CASE WHEN bench1 = :playerId THEN NULL ELSE bench1 END, " +
            "bench2 = CASE WHEN bench2 = :playerId THEN NULL ELSE bench2 END, " +
            "bench3 = CASE WHEN bench3 = :playerId THEN NULL ELSE bench3 END, " +
            "bench4 = CASE WHEN bench4 = :playerId THEN NULL ELSE bench4 END " +
            "WHERE :playerId IN (qb_player_id, rb1_player_id, rb2_player_id, wr1_player_id, wr2_player_id, te_player_id, flex_player_id, kicker_player_id, def_player_id, bench1, bench2, bench3, bench4)",
            nativeQuery = true)
        void removePlayerFromRoster(@Param("playerId") Integer playerId);

    @Query(value = "SELECT * FROM rosters WHERE " +
            "qb_player_id = :playerId OR rb1_player_id = :playerId OR rb2_player_id = :playerId OR " +
            "wr1_player_id = :playerId OR wr2_player_id = :playerId OR te_player_id = :playerId OR flex_player_id = :playerId OR " +
            "kicker_player_id = :playerId OR def_player_id = :playerId OR " +
            "bench1 = :playerId OR bench2 = :playerId OR bench3 = :playerId OR bench4 = :playerId " +
            "LIMIT 1", nativeQuery = true)
    Optional<Rosters> findRosterByPlayerId(@Param("playerId") Integer playerId);
}
