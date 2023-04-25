package com.carni.elo.engine.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Query(value = "SELECT * FROM game e where e.player1name = :player OR " +
            "e.player2name = :player ORDER BY e.id DESC LIMIT :count", nativeQuery = true)
    List<Game> findRecentGames(@Param("count") int count, @Param("player") String player);
}