package com.elessarov.telegram_bot_spring_boot_starter.util;

import com.elessarov.telegram_bot_spring_boot_starter.model.Button;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class with helper methods for working with Telegram bot messages and keyboards.
 */
public final class BotUtils {

    private BotUtils() {
    }

    /**
     * Retrieves the chat ID from a message update as a String.
     *
     * @param update the Telegram update containing the message.
     * @return the chat ID as a String.
     */
    public static String getChatId(Update update) {
        return update.getMessage().getChatId().toString();
    }

    /**
     * Retrieves the chat ID from a callback query update as a String.
     *
     * @param update the Telegram update containing the callback query.
     * @return the chat ID as a String.
     */
    public static String getCallbackChatId(Update update) {
        return update.getCallbackQuery().getMessage().getChatId().toString();
    }

    /**
     * Retrieves the username of the user who triggered a callback query.
     *
     * @param update the Telegram update containing the callback query.
     * @return the username of the callback sender.
     */
    public static String getUserNameFromCallback(Update update) {
        return update.getCallbackQuery().getFrom().getUserName();
    }

    /**
     * Adds an inline keyboard to the provided SendMessage object.
     * The buttons are arranged in rows with the specified number of buttons per row.
     *
     * @param sendMessage   the SendMessage object to which the keyboard is added.
     * @param keyBoardList  the list of buttons to add.
     * @param buttonsPerRow the number of buttons per row.
     * @throws RuntimeException if any button text exceeds 30 characters.
     */
    public static void addInlineKeyBoard(SendMessage sendMessage, List<Button> keyBoardList, int buttonsPerRow) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();

        for (int i = 0; i < keyBoardList.size(); i++) {
            Button button = keyBoardList.get(i);
            if (button.name().length() > 30) {
                throw new RuntimeException("Button text '%s' must not exceed 30 characters".formatted(button.name()));
            }
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(button.name());
            inlineKeyboardButton.setCallbackData(button.callbackData());
            keyboardButtonsRow.add(inlineKeyboardButton);

            if ((i + 1) % buttonsPerRow == 0 || i == keyBoardList.size() - 1) {
                rowList.add(new ArrayList<>(keyboardButtonsRow));
                keyboardButtonsRow.clear();
            }
        }

        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    }

    /**
     * Adds a reply keyboard to the provided SendMessage object.
     * Depending on the number of buttons and specified buttons per row, the keyboard will be arranged accordingly.
     *
     * @param sendMessage   the SendMessage object to which the keyboard is added.
     * @param keyBoardList  the list of button texts.
     * @param buttonsPerRow the number of buttons per row.
     */
    public static void addKeyBoard(SendMessage sendMessage, List<String> keyBoardList, int buttonsPerRow) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        if (keyBoardList.size() <= buttonsPerRow) {
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.addAll(keyBoardList);
            keyboardRows.add(keyboardRow);
        } else {
            KeyboardRow keyboardRow = new KeyboardRow();
            int counter = 0;
            for (String item : keyBoardList) {
                keyboardRow.add(item);
                counter++;
                if (counter % buttonsPerRow == 0) {
                    keyboardRows.add(keyboardRow);
                    keyboardRow = new KeyboardRow();
                }
            }
            if (!keyboardRow.isEmpty()) {
                keyboardRows.add(keyboardRow);
            }
        }
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

    /**
     * Removes the keyboard from the user's screen by setting a ReplyKeyboardRemove as the reply markup.
     *
     * @param sendMessage the SendMessage object for which the keyboard should be removed.
     */
    public static void removeKeyboard(SendMessage sendMessage) {
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);
        sendMessage.setReplyMarkup(replyKeyboardRemove);
    }

    /**
     * Sets a list of commands for the bot menu
     *
     * @param bot       your bot (instance of AbsSender)
     * @param commands  list of commands to be placed in the menu
     * @throws TelegramApiException if an error occurred while executing the request
     */
    public static void setCommands(AbsSender bot, List<BotCommand> commands) throws TelegramApiException {
        if (commands == null || commands.isEmpty()) {
            return;
        }
        SetMyCommands setMyCommands = new SetMyCommands(commands, null, null);
        bot.execute(setMyCommands);
    }
}
