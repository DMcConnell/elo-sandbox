package com.carni.elo.engine.data;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;

    private int currentElo;

    private int gamesPlayed;
    private int gamesWon;
    private int gamesLost;
}
