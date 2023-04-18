package com.carni.elo.engine.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Query(value = "SELECT * FROM player e where e.name = :name", nativeQuery = true)
    Player findByName(@Param("name") String name);

    @Query(value = "SELECT * FROM player e ORDER BY e.current_elo DESC LIMIT :limit", nativeQuery = true)
    List<Player> getPlayersRankedByElo(@Param("limit") int limit);

    @Query(value = "SELECT * FROM player e ORDER BY e.current_streak DESC LIMIT :limit", nativeQuery = true)
    List<Player> getPlayersRankedByStreak(@Param("limit") int limit);
}