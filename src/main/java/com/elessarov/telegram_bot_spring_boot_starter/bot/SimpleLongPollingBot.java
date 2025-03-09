package com.elessarov.telegram_bot_spring_boot_starter.bot;

import com.elessarov.telegram_bot_spring_boot_starter.properties.BotProperties;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class SimpleLongPollingBot extends TelegramLongPollingBot {
    private final BotProperties botProperties;
    private final UpdateHandler updateHandler;

    public SimpleLongPollingBot(BotProperties botProperties, UpdateHandler updateHandler) {
        super(botProperties.getToken());
        this.botProperties = botProperties;
        this.updateHandler = updateHandler;
    }

    @Override
    public void onUpdateReceived(Update update) {
       sendMessage(updateHandler.handle(update));
    }

    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }

    public void sendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
