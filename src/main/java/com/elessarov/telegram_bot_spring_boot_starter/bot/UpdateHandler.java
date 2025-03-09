package com.elessarov.telegram_bot_spring_boot_starter.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {
    SendMessage handle(Update update);

    default boolean isTextMessage(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    default boolean isCallbackQuery(Update update) {
        return update.hasCallbackQuery();
    }

    default boolean isDocumentMessage(Update update) {
        return update.hasMessage() && update.getMessage().hasDocument();
    }
}
