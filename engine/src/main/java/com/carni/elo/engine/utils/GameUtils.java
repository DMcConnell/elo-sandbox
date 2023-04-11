package com.carni.elo.engine.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameUtils {

    public enum VALID_HANDS {
        ROCK, PAPER, SCISSORS
    }

    public static boolean isValidHand(String hand) {
        for (VALID_HANDS validHand : VALID_HANDS.values()) {
            if (validHand.name().equals(hand)) {
                return true;
            }
        }
        return false;
    }

    public static boolean didPlayer1Win(String player1Hand, String player2Hand) {
        log.info("didPlayer1Win: player1Hand: {}, player2Hand: {}", player1Hand, player2Hand);
        if (player1Hand.equals(player2Hand)) {
            return false;
        }
        if (player1Hand.equals(VALID_HANDS.ROCK.name())) {
            log.debug("didPlayer1Win: registered rock, returning: {}", player2Hand.equals(VALID_HANDS.SCISSORS.name()));
            return player2Hand.equals(VALID_HANDS.SCISSORS.name());
        }
        if (player1Hand.equals(VALID_HANDS.PAPER.name())) {
            log.debug("didPlayer1Win: registered paper, returning: {}", player2Hand.equals(VALID_HANDS.ROCK.name()));
            return player2Hand.equals(VALID_HANDS.ROCK.name());
        }
        if (player1Hand.equals(VALID_HANDS.SCISSORS.name())) {
            log.debug("didPlayer1Win: registered scissors, returning: {}", player2Hand.equals(VALID_HANDS.PAPER.name()));
            return player2Hand.equals(VALID_HANDS.PAPER.name());
        }
        return false;
    }
}
