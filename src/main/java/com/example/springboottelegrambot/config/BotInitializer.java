package com.example.springboottelegrambot.config;

import com.example.springboottelegrambot.service.TelegramBotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Slf4j
public class BotInitializer {

    private final TelegramBotService telegramBot;

    public BotInitializer(TelegramBotService telegramBotService) {
        this.telegramBot = telegramBotService;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        try {
            var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBot);
            log.info("The bot is registered successfully");
        } catch (TelegramApiException e) {
            log.error("Error occurred during registering the bot: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}