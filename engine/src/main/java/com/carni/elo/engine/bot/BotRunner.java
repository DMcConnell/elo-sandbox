package com.carni.elo.engine.bot;

import lombok.extern.slf4j.Slf4j;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class BotRunner implements CommandLineRunner {

    private final String api_token = "xxx";

    @Autowired
    private BotService botService;

    @Value("${frontend.run-bot}")
    private boolean runBot;

    @Override
    public void run(String... args) throws Exception {
        if(!runBot) {
            log.warn("BotRunner: Bot set to not run, exiting");
            return;
        }

        DiscordApi api = new DiscordApiBuilder().addIntents(Intent.MESSAGE_CONTENT).setToken(api_token).login().join();

        api.addMessageCreateListener(event -> {
            if (event.getMessage().getContent().equalsIgnoreCase("!ping")) {
                event.getChannel().sendMessage("Pong!");
            }
            List<String> commandArgs = parseCommand(event.getMessage().getContent());
            switch (commandArgs.get(0)) {
                case "addPlayer" -> {
                    botService.addPlayer(commandArgs.get(1));
                    event.getChannel().sendMessage("Added player: " + commandArgs.get(1));
                }
                case "listPlayers" -> botService.listPlayers(event.getChannel());
                case "recordGame" -> botService.recordGame(event.getChannel(), commandArgs.get(1), commandArgs.get(2),
                        commandArgs.get(3), commandArgs.get(4));
                case "listGames" -> botService.listGames(event.getChannel());
                case "getLeaderboard" -> botService.getLeaderboard(event.getChannel(), commandArgs);
                default -> event.getChannel().sendMessage("Unknown command: " + commandArgs.get(0));
            }
        });

        System.out.println("You can invite the bot by using the following url: " + api.createBotInvite());
    }

    protected List<String> parseCommand(String message) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < args.size(); i++) {
//            sb.append(i).append("=").append(args.get(i)).append("\t");
//        }
//        log.info("Args: " + sb.toString());
        return splitArgs(message.substring(3).trim());
    }

    private List<String> splitArgs(String fullLine) {
        List<String> argsList = new ArrayList<>();
        String[] cmds = fullLine.split(" ");
        for (int i = 0; i < cmds.length; i++) {
            if (cmds[i].isBlank()) {
                continue;
            }
            if (cmds[i].charAt(0) == '\'') {
                if (cmds[i].substring(cmds[i].length() - 1).equals("'")) {
                    argsList.add(cmds[i].substring(1, cmds[i].length() - 1));
                } else {
                    String fullArg = cmds[i].substring(1);
                    for (int j = i + 1; j < cmds.length; j++) {
                        if (cmds[j].substring(cmds[j].length() - 1).equals("'")) {
                            fullArg = fullArg + " " + cmds[j].substring(0, cmds[j].length() - 1);
                            argsList.add(fullArg);
                            i = j;
                            break;
                        } else {
                            fullArg = fullArg + " " + cmds[j];
                        }
                    }
                }
            } else {
                argsList.add(cmds[i]);
            }
        }
        return argsList;
    }
}
