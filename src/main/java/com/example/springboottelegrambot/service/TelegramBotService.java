package com.example.springboottelegrambot.service;

import com.example.springboottelegrambot.config.BotConfig;
import com.example.springboottelegrambot.model.User;
import com.example.springboottelegrambot.model.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class TelegramBotService extends TelegramLongPollingBot {

    private final UserRepository userRepository;

    private final BotConfig botConfig;

    public TelegramBotService(BotConfig botConfig, UserRepository userRepository) {

        this.botConfig = botConfig;
        this.userRepository = userRepository;

        // menu
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/start", "get hello message from bot"));
        commands.add(new BotCommand("/bye", "get bye message from bot"));
        commands.add(new BotCommand("/help", "info how to use this bot"));
        commands.add(new BotCommand("/settings", "set your preferences"));

        try {
            this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error occurred during setting commands: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    private void registerUser(Message msg) {

        if (userRepository.findById(msg.getChatId()).isEmpty()) {

            var user = new User();
            user.setChatId(msg.getChatId());
            user.setFirstName(msg.getFrom().getUserName());
            user.setUsername(msg.getFrom().getFirstName());
            user.setLastName(msg.getFrom().getLastName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);

            log.info("new user " + user + " is added");
        }
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

        registerUser(message);

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
            this.execute(sendMessage);
            log.info("message sent to user");
        } catch (TelegramApiException e) {
            log.error("Error occurred during replying to user: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
