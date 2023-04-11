package com.carni.elo.engine.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Query(value = "SELECT * FROM player e where e.name = :name", nativeQuery = true)
    Player findByName(@Param("name") String name);
}