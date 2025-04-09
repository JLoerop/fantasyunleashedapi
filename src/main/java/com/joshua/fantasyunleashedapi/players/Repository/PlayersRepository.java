package com.joshua.fantasyunleashedapi.players.Repository;

import com.joshua.fantasyunleashedapi.players.Model.Players;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayersRepository extends JpaRepository<Players, Integer> {
    Optional<Players> findByName(String name);
    Optional<Players> findByTeamAndPosition(String team, String position);
}
