package com.carni.elo.engine.service;

import com.carni.elo.engine.data.EloLog;
import com.carni.elo.engine.data.EloLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Date;
@Slf4j
@Service
public record EloService(EloLogRepository eloLogRepository) {

    private static final int K_FACTOR = 32;

    public Pair<Integer, Integer> updateElo(int player1Elo, int player2Elo, boolean didPlayer1Win) {
        return Pair.of(calculateElo(player1Elo, player2Elo, didPlayer1Win ? 1 : 0),
                calculateElo(player2Elo, player1Elo, didPlayer1Win ? 0 : 1));
    }

    private int calculateElo(int playerRating, int opponentRating, int result) {
        // Calculate expected score for the player based on their rating and the opponent's rating
        double expectedScore = 1.0 / (1.0 + Math.pow(10.0, (opponentRating - playerRating) / 400.0));

        // Calculate actual score based on the game result (1 for win, 0.5 for draw, 0 for loss)
        double actualScore;
        if (result == 1) {
            actualScore = 1.0;
        } else if (result == 0.5) {
            actualScore = 0.5;
        } else {
            actualScore = 0.0;
        }

        // Calculate the new rating based on the player's current rating, K-factor, and the difference between actual and expected scores

        return playerRating + (int) Math.round(K_FACTOR * (actualScore - expectedScore));
    }

    public long recordEloLog(long playerId, int oldElo, int newElo) {
        EloLog newLog = EloLog.builder()
                .playerId(playerId)
                .oldElo(oldElo)
                .newElo(newElo)
                .eloChange(newElo - oldElo)
                .timestamp(new Date())
                .build();
        return eloLogRepository.save(newLog).getId();
    }
}
