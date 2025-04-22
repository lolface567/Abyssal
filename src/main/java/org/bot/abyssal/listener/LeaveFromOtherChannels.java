package org.bot.abyssal.listener;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Service;

@Service
public class LeaveFromOtherChannels extends ListenerAdapter {

    private final long ALLOWED_GUILD_ID;

    public LeaveFromOtherChannels(Dotenv dotenv) {
        this.ALLOWED_GUILD_ID = Long.parseLong(dotenv.get("ALLOWED_GUILD_ID"));
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        if (ALLOWED_GUILD_ID != event.getGuild().getIdLong()) {
            event.getGuild().leave().queue();
        }
    }
}