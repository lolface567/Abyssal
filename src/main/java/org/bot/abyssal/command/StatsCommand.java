package org.bot.abyssal.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bot.abyssal.service.StatsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatsCommand extends ListenerAdapter {
    private final StatsService statsService;

    @Autowired
    public StatsCommand(StatsService statsService) {
        this.statsService = statsService;
    }
    
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("stats")) {
            statsService.printStats(event);
        }
    }
}