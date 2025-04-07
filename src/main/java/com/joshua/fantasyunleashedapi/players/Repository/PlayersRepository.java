package com.joshua.fantasyunleashedapi.players.Repository;

import com.joshua.fantasyunleashedapi.players.Model.Players;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayersRepository extends JpaRepository<Players, Integer> {

}
