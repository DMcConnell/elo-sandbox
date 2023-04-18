package com.carni.elo.engine.bot;

import com.carni.elo.engine.controller.EloEngineController;
import com.carni.elo.engine.data.Game;
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
        if(args.size() == 1) {
            MessageBuilder mb = new MessageBuilder().append(eloEngineController.getPlayersRanked(5, "elo"));
            mb.send(channel);
        } else if(args.size() == 2) {
            MessageBuilder mb = new MessageBuilder().append(eloEngineController.getPlayersRanked(5, args.get(1)));
            mb.send(channel);
        } else {
            MessageBuilder mb = new MessageBuilder().append("Invalid command");
            mb.send(channel);
        }
    }
}
