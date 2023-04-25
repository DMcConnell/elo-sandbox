package com.carni.elo.engine.bot;

import com.carni.elo.engine.controller.EloEngineController;
import com.carni.elo.engine.data.Game;
import com.carni.elo.engine.data.Player;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BotService {

    @Autowired
    private EloEngineController eloEngineController;

    public void addPlayer(String name) {
        eloEngineController.createPlayer(name);
    }

    public void listPlayers(TextChannel textChannel) {
        MessageBuilder mb = new MessageBuilder().append(eloEngineController.getPlayers());
        mb.send(textChannel);
    }

    public void recordGame(TextChannel textChannel, String player1Name,
                           String player1Hand, String player2Name, String player2Hand) {
        Game game = new Game();
        game.setPlayer1Name(player1Name);
        game.setPlayer1Hand(player1Hand);
        game.setPlayer2Name(player2Name);
        game.setPlayer2Hand(player2Hand);

        eloEngineController.recordGame(game);

        MessageBuilder mb = new MessageBuilder().append("Game recorded: " + game);
        mb.send(textChannel);
    }

    public void listGames(TextChannel channel) {
        MessageBuilder mb = new MessageBuilder().append(eloEngineController.getGames());
        mb.send(channel);
    }

    public void getLeaderboard(TextChannel channel, List<String> args) {
        List<Player> players;

        if(args.size() == 1) {
            players = eloEngineController.getPlayersRanked(5, "elo");
        } else if(args.size() == 2) {
            players = eloEngineController.getPlayersRanked(5, args.get(1));
        } else {
            MessageBuilder mb = new MessageBuilder().append("Invalid command");
            mb.send(channel);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < players.size(); i++) {
            sb.append(i + 1).append(". ").append(players.get(i).getName()).append(" (").append(players.get(i).getCurrentElo()).append(")\n");
        }
        MessageBuilder mb = new MessageBuilder().append(sb.toString());
        mb.send(channel);
    }

    public void showRecent(TextChannel channel, List<String> args) {
        //extract player name as first arg or post error message
        if(args.size() < 2) {
            MessageBuilder mb = new MessageBuilder().append("Invalid command");
            mb.send(channel);
            return;
        }

        String player = args.get(1);
        int count = 3;
        if(args.size() == 3) {
            count = Integer.parseInt(args.get(2));
        }

        List<Game> recentGames = eloEngineController.getRecentGames(count, player);
        StringBuilder sb = new StringBuilder();
        for (Game game : recentGames) {
            if(game.getPlayer1Name().equals(player)) {
                sb.append(game.getPlayer1Hand()).append("\n");
            } else {
                sb.append(game.getPlayer2Hand()).append("\n");
            }
        }
        MessageBuilder mb = new MessageBuilder().append(sb.toString());
        mb.send(channel);
    }
}
