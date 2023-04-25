package com.carni.elo.engine.service;

import com.carni.elo.engine.data.Game;
import com.carni.elo.engine.data.GameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public record GameService(GameRepository gameRepository) {

    public void recordGame(Game game) {
        gameRepository.save(game);
    }

    public Game getGameById(long id) {
        return gameRepository.findById(id).orElse(null);
    }

    public List<Game> getGames() {
        return gameRepository.findAll();
    }

    public List<Game> getRecentGames(int count, String player) {
        return gameRepository.findRecentGames(count, player);
    }
}
