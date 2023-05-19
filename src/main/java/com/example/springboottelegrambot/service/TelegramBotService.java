package com.example.springboottelegrambot.service;

import com.example.springboottelegrambot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class TelegramBotService extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    public TelegramBotService(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (!update.hasMessage())
            return;

        Message message = update.getMessage();

        if (!message.hasText())
            return;

        String messageText = message.getText();
        String chatIt = String.valueOf(message.getChatId());

        if (messageText.equals("/start")) {
            commandReceived(chatIt, "hello, " + message.getFrom().getFirstName());
        } else if (messageText.equals("/bye")) {
            commandReceived(chatIt, "good bye");
        } else {
            commandReceived(chatIt, "I don't understand you");
        }
    }

    private void commandReceived(String chatIt, String text) {
        log.info("command received from user");
        sendMessage(chatIt, text);
    }

    private void sendMessage(String chatIt, String text) {

        SendMessage sendMessage = new SendMessage(chatIt, text);

        try {
            execute(sendMessage);
            log.info("message sent to user");
        } catch (TelegramApiException e) {
            log.error("Error occurred during replying to user: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
