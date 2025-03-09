package com.elessarov.telegram_bot_spring_boot_starter;

import com.elessarov.telegram_bot_spring_boot_starter.model.Button;
import com.elessarov.telegram_bot_spring_boot_starter.util.BotUtils;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BotUtilsTest {

    @Test
    void testGetChatId() {
        Chat chat = new Chat();
        chat.setId(123456789L);

        Message message = new Message();
        message.setChat(chat);

        Update update = new Update();
        update.setMessage(message);

        String chatId = BotUtils.getChatId(update);
        assertEquals("123456789", chatId);
    }

    @Test
    void testGetCallbackChatIdAndUserNameFromCallback() {
        Chat chat = new Chat();
        chat.setId(987654321L);

        Message message = new Message();
        message.setChat(chat);

        User user = new User();
        user.setUserName("testUser");

        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setMessage(message);
        callbackQuery.setFrom(user);

        Update update = new Update();
        update.setCallbackQuery(callbackQuery);

        String callbackChatId = BotUtils.getCallbackChatId(update);
        String userName = BotUtils.getUserNameFromCallback(update);

        assertEquals("987654321", callbackChatId);
        assertEquals("testUser", userName);
    }

    @Test
    void testAddInlineKeyBoard() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Test message");

        List<Button> buttons = Arrays.asList(
                new Button("Button1", "callbackData1"),
                new Button("Button2", "callbackData2"),
                new Button("Button3", "callbackData3")
        );

        BotUtils.addInlineKeyBoard(sendMessage, buttons, 2);

        assertNotNull(sendMessage.getReplyMarkup());
        assertInstanceOf(InlineKeyboardMarkup.class, sendMessage.getReplyMarkup());

        InlineKeyboardMarkup markup = (InlineKeyboardMarkup) sendMessage.getReplyMarkup();
        assertEquals(2, markup.getKeyboard().size());
        assertEquals(2, markup.getKeyboard().get(0).size());
        assertEquals(1, markup.getKeyboard().get(1).size());
    }

    @Test
    void testAddKeyBoard() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Test message");

        List<String> buttonTexts = Arrays.asList("A", "B", "C", "D", "E");

        BotUtils.addKeyBoard(sendMessage, buttonTexts, 3);

        assertNotNull(sendMessage.getReplyMarkup());
        assertInstanceOf(ReplyKeyboardMarkup.class, sendMessage.getReplyMarkup());

        ReplyKeyboardMarkup markup = (ReplyKeyboardMarkup) sendMessage.getReplyMarkup();
        List<KeyboardRow> rows = markup.getKeyboard();

        assertEquals(2, rows.size());
        assertEquals(3, rows.get(0).size());
        assertEquals(2, rows.get(1).size());
    }

    @Test
    void testRemoveKeyboard() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Test message");

        sendMessage.setReplyMarkup(new ReplyKeyboardMarkup());

        BotUtils.removeKeyboard(sendMessage);

        assertNotNull(sendMessage.getReplyMarkup());
        assertInstanceOf(ReplyKeyboardRemove.class, sendMessage.getReplyMarkup());
    }
}
