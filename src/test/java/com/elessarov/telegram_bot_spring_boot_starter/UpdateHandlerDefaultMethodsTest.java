package com.elessarov.telegram_bot_spring_boot_starter;

import com.elessarov.telegram_bot_spring_boot_starter.bot.UpdateHandler;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateHandlerDefaultMethodsTest {

    // A simple UpdateHandler implementation for testing default methods
    private final UpdateHandler handler = update -> null;

    @Test
    void isTextMessage_whenMessageWithText_thenTrue() {
        Update update = new Update();
        Message message = new Message();
        message.setText("Hello");
        update.setMessage(message);

        assertTrue(handler.isTextMessage(update));
    }

    @Test
    void isTextMessage_whenNoMessage_thenFalse() {
        Update update = new Update();
        assertFalse(handler.isTextMessage(update));
    }

    @Test
    void isTextMessage_whenMessageWithoutText_thenFalse() {
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);

        assertFalse(handler.isTextMessage(update));
    }

    @Test
    void isCallbackQuery_whenCallbackPresent_thenTrue() {
        Update update = new Update();
        CallbackQuery callback = new CallbackQuery();
        update.setCallbackQuery(callback);

        assertTrue(handler.isCallbackQuery(update));
    }

    @Test
    void isCallbackQuery_whenNoCallback_thenFalse() {
        Update update = new Update();
        assertFalse(handler.isCallbackQuery(update));
    }

    @Test
    void isDocumentMessage_whenMessageWithDocument_thenTrue() {
        Update update = new Update();
        Message message = new Message();
        Document doc = new Document();
        message.setDocument(doc);
        update.setMessage(message);

        assertTrue(handler.isDocumentMessage(update));
    }

    @Test
    void isDocumentMessage_whenNoMessage_thenFalse() {
        Update update = new Update();
        assertFalse(handler.isDocumentMessage(update));
    }

    @Test
    void isDocumentMessage_whenMessageWithoutDocument_thenFalse() {
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);

        assertFalse(handler.isDocumentMessage(update));
    }
}
