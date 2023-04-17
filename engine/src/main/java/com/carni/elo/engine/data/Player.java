package com.carni.elo.engine.data;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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

    @ColumnDefault("1000")
    private int highestElo;

    private int gamesPlayed;
    private int gamesWon;
    private int gamesLost;

    @ColumnDefault("0")
    private int currentStreak;
    @ColumnDefault("0")
    private int longestStreak;
}
