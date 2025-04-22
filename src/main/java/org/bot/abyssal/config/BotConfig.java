package org.bot.abyssal.config;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BotConfig {
    private static final Logger logger = LoggerFactory.getLogger(BotConfig.class);

    @Autowired
    public BotConfig(List<ListenerAdapter> eventListeners, DotenvConfig config) {
        // Load environment variables
        Dotenv dotenv = Dotenv.configure()
                .filename("settings.env")
                .load();

        // Build the JDA instance
        JDA jda = JDABuilder.createDefault(dotenv.get("TOKEN")) // Вместо createLight
                .enableIntents(GatewayIntent.GUILD_VOICE_STATES) // Включаем голосовые события
                .enableIntents(GatewayIntent.GUILD_MESSAGES)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .enableIntents(GatewayIntent.GUILD_MESSAGE_REACTIONS)
                .enableIntents(GatewayIntent.GUILD_MEMBERS) // Кэшируем мемберов
                .setMemberCachePolicy(MemberCachePolicy.ALL) // Полное кэширование участников
                .enableCache(CacheFlag.VOICE_STATE) // Включаем кэширование голосовых каналов
                .addEventListeners(eventListeners.toArray())
                .build();

        logger.info("Bot Started!");
        logger.info("Version 1.0.0");

        logger.info("DataBase successfully connected");

        // Add slash commands
        CommandListUpdateAction commands = jda.updateCommands();
        commands.addCommands(
                Commands.slash("shop", "Открывает магазин сервера"),
                Commands.slash("give", "Перевести монеты")
                        .addOption(OptionType.STRING, "id_user", "Айди юзера")
                        .addOption(OptionType.STRING, "count", "Количество"),
                Commands.slash("check_coins", "Проверить количество монет")
                        .addOption(OptionType.STRING, "id_user", "Айди юзера"),
                Commands.slash("remove_role_from_shop", "Убрать роль с продажи")
                        .addOption(OptionType.STRING, "role_id", "Айди роли")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                Commands.slash("add_role_to_shop", "Выставить роль на продажу")
                        .addOption(OptionType.STRING, "role_id", "Айди роли")
                        .addOption(OptionType.STRING, "role_cost", "Цена")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                Commands.slash("stats", "Открывает вашу статистику на сервере"),
                Commands.slash("add_coins", "Прибавляет монеты юзеру")
                        .addOption(OptionType.STRING, "id", "Айди юзера")
                        .addOption(OptionType.STRING, "coins", "Количество")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
        );
        commands.queue();
    }
}
