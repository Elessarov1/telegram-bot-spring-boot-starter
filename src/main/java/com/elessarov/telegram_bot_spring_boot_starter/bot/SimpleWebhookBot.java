package com.elessarov.telegram_bot_spring_boot_starter.bot;

import com.elessarov.telegram_bot_spring_boot_starter.properties.BotProperties;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;


public class SimpleWebhookBot extends SpringWebhookBot {
    private final BotProperties botProperties;
    private final UpdateHandler updateHandler;

    public SimpleWebhookBot(
            BotProperties botProperties,
            SetWebhook setWebhook,
            UpdateHandler updateHandler
    ) {
        super(setWebhook, botProperties.getToken());
        this.botProperties = botProperties;
        this.updateHandler = updateHandler;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return updateHandler.handle(update);
    }

    @Override
    public String getBotPath() {
        return BotProperties.Webhook.DEFAULT_PATH;
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
