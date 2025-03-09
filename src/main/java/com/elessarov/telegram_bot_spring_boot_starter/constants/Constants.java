package com.elessarov.telegram_bot_spring_boot_starter.constants;

public class Constants {
    public static final String BOT_TYPE = "telegram.bot.type";
    public static final String WEBHOOK_BOT_TYPE = "Webhook";;
    public static final String LONG_POLLING_BOT_TYPE = "LongPolling";
    public static final String SET_WEBHOOK_URL = "https://api.telegram.org/bot%s/setWebhook";
    public static final String UPDATE_HANDLER_BEAN_MISSING = "Update received but your UpdateHandler bean is not configured";

    private Constants() {
    }
}
