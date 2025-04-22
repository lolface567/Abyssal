package org.bot.abyssal.service;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class StatsService extends ListenerAdapter {
    private final UserCoinService userCoinService;

    @Autowired
    public StatsService(UserCoinService userCoinService) {
        this.userCoinService = userCoinService;
    }

    public void printStats(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        if (member == null) {
            event.reply("Ошибка: не удалось получить информацию о пользователе.").setEphemeral(true).queue();
            return;
        }

        String balance = String.valueOf(userCoinService.getBalance(member.getIdLong())); // Получаем баланс
        LocalDate joinDate = member.getTimeJoined().toLocalDate();
        LocalDate today = LocalDate.now();
        long daysOnServer = ChronoUnit.DAYS.between(joinDate, today); // Считаем дни

        String creationDate = member.getUser().getTimeCreated().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        String roles = member.getRoles().isEmpty() ? "Нет ролей" :
                member.getRoles().stream().map(Role::getName).collect(Collectors.joining(", "));

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("👤 Статистика пользователя: " + member.getEffectiveName())
                .setThumbnail(member.getEffectiveAvatarUrl()) // Аватарка
                .setColor(Color.cyan) // Цвет рамки
                .addField("💰 Баланс:", balance + " монет", false)
                .addField("⏳ Вы с нами уже:", daysOnServer + " дней", false)
                .addField("📆 Дата создания аккаунта:", creationDate, false)
                .addField("🏅 Роли:", roles, false)
                .setFooter("ID: " + member.getId(), member.getEffectiveAvatarUrl());

        event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
    }
}
