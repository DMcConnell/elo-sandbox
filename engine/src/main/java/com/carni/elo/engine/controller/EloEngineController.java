package com.carni.elo.engine.controller;

import com.carni.elo.engine.data.Game;
import com.carni.elo.engine.data.Player;
import com.carni.elo.engine.service.EloService;
import com.carni.elo.engine.service.GameService;
import com.carni.elo.engine.service.PlayerService;
import com.carni.elo.engine.utils.GameUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public record EloEngineController(PlayerService playerService, GameService gameService, EloService eloService) {

    @PostMapping("/api/elo/v1/player")
    public void createPlayer(@RequestParam(name = "name") String name) {
        log.info("createPlayer: name: {}", name);
        if (name != null && !name.isBlank()) {
            playerService.createPlayer(name);
        } else {
            log.error("createPlayer: name is null or blank");
        }
    }

    @GetMapping("/api/elo/v1/players")
    public List<Player> getPlayers() {
        log.info("getPlayers");
        return playerService.getPlayers();
    }

    @PostMapping("/api/elo/v1/game")
    public void recordGame(@RequestBody Game game) {
        log.info("recordGame: game: {}", game);

        boolean player1Valid = playerService.doesPlayerExist(game.getPlayer1Name());
        boolean player2Valid = playerService.doesPlayerExist(game.getPlayer2Name());
        boolean player1ValidHand = GameUtils.isValidHand(game.getPlayer1Hand().toUpperCase());
        boolean player2ValidHand = GameUtils.isValidHand(game.getPlayer2Hand().toUpperCase());

        if (player1Valid && player2Valid && player1ValidHand && player2ValidHand) {
            log.info("recordGame: game is valid, attaching elo + recording");

            game.setPlayer1Elo(playerService.getPlayerElo(game.getPlayer1Name()));
            game.setPlayer2Elo(playerService.getPlayerElo(game.getPlayer2Name()));

            game.setDidPlayer1Win(GameUtils.didPlayer1Win(game.getPlayer1Hand().toUpperCase(),
                    game.getPlayer2Hand().toUpperCase()));

            Pair<Integer, Integer> newElos = eloService.updateElo(game.getPlayer1Elo(), game.getPlayer2Elo(),
                    game.getDidPlayer1Win());

            long player1LogId = eloService.recordEloLog(playerService.getPlayerId(game.getPlayer1Name()),
                    game.getPlayer1Elo(), newElos.getFirst());
            long player2LogId = eloService.recordEloLog(playerService.getPlayerId(game.getPlayer2Name()),
                    game.getPlayer2Elo(), newElos.getSecond());

            game.setPlayer1EloLogId(player1LogId);
            game.setPlayer2EloLogId(player2LogId);

            playerService.updatePlayerElo(game.getPlayer1Name(), newElos.getFirst());
            playerService.updatePlayerElo(game.getPlayer2Name(), newElos.getSecond());

            playerService.recordGamePlayed(game.getPlayer1Name(), game.getDidPlayer1Win());
            playerService.recordGamePlayed(game.getPlayer2Name(), !game.getDidPlayer1Win());

            gameService.recordGame(game);

            log.info("recordGame: game recorded: {}, updating player elo", game);
        } else {
            log.error("recordGame: invalid game");
        }
    }

    @GetMapping("/api/elo/v1/games")
    public List<Game> getGames() {
        log.info("getGames");
        return gameService.getGames();
    }
}
