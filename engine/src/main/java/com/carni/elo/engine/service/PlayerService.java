package com.carni.elo.engine.service;

import com.carni.elo.engine.data.Player;
import com.carni.elo.engine.data.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public record PlayerService(PlayerRepository playerRepository) {

    public void createPlayer(String name) {
        log.info("createPlayer: name: {}", name);

        if (doesPlayerExist(name)) {
            log.error("createPlayer: player already exists");
            return;
        }

        Player newPlayer = new Player();
        newPlayer.setName(name);
        newPlayer.setCurrentElo(1000);
        newPlayer.setGamesLost(0);
        newPlayer.setGamesWon(0);
        newPlayer.setGamesPlayed(0);
        newPlayer.setHighestElo(1000);
        newPlayer.setCurrentStreak(0);
        newPlayer.setLongestStreak(0);
        playerRepository.save(newPlayer);
    }

    public boolean doesPlayerExist(String name) {
        log.info("doesPlayerExist: name: {}", name);
        return playerRepository.findByName(name) != null;
    }

    public int getPlayerElo(String name) {
        log.info("getPlayerElo: name: {}", name);
        int elo = -1;
        try {
            elo = playerRepository.findByName(name).getCurrentElo();
        } catch (Exception e) {
            log.error("player does not exist");
        }
        return elo;
    }

    public void updatePlayerElo(String name, int newElo) {
        log.info("updatePlayerElo: name: {}, newElo: {}", name, newElo);
        Player player = playerRepository.findByName(name);
        player.setCurrentElo(newElo);

        if (player.getCurrentElo() > player.getHighestElo()) {
            player.setHighestElo(player.getCurrentElo());
        }
        playerRepository.save(player);
    }

    public long getPlayerId(String player1Name) {
        log.info("getPlayerId: name: {}", player1Name);
        return playerRepository.findByName(player1Name).getId();
    }

    public List<Player> getPlayers() {
        log.info("getPlayers");
        return playerRepository.findAll();
    }

    public void recordGamePlayed(String playerName, boolean won) {
        log.info("recordGamePlayed: playerName: {}, won: {}", playerName, won);
        Player player = playerRepository.findByName(playerName);
        player.setGamesPlayed(player.getGamesPlayed() + 1);
        if (won) {
            player.setGamesWon(player.getGamesWon() + 1);

            player.setCurrentStreak(player.getCurrentStreak() + 1);
            if (player.getCurrentStreak() > player.getLongestStreak()) {
                player.setLongestStreak(player.getCurrentStreak());
            }
        } else {
            player.setGamesLost(player.getGamesLost() + 1);
            player.setCurrentStreak(0);
        }
        playerRepository.save(player);
    }
}
