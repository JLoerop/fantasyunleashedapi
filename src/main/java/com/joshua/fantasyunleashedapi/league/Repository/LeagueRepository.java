package com.joshua.fantasyunleashedapi.league.Repository;

import com.joshua.fantasyunleashedapi.league.Model.League;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueRepository extends JpaRepository<League, Integer> {
}
