package com.example.springboottelegrambot.service;

import com.example.springboottelegrambot.config.BotConfig;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    public TelegramBot(BotConfig botConfig) {
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

        if (messageText.equals("/start")) {
            sendMessage(message.getChatId(), "hello");
        } else if (messageText.equals("/bye")) {
            sendMessage(message.getChatId(), "good bye");
        } else {
            sendMessage(message.getChatId(), "I don't understand you");
        }
    }

    @SneakyThrows
    private void sendMessage(long chatIt, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatIt));
        sendMessage.setText(text);
        execute(sendMessage);
    }
}
