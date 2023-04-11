package com.carni.elo.engine.data;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String player1Name;
    private String player2Name;
    private int player1Elo;
    private int player2Elo;
    private Long player1EloLogId;
    private Long player2EloLogId;

    private boolean didPlayer1Win;
    private String player1Hand;
    private String player2Hand;

    public boolean getDidPlayer1Win() {
        return didPlayer1Win;
    }
}
