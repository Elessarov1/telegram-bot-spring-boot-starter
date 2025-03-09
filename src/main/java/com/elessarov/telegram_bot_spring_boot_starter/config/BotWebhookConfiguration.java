package com.elessarov.telegram_bot_spring_boot_starter.config;

import com.elessarov.telegram_bot_spring_boot_starter.bot.UpdateHandler;
import com.elessarov.telegram_bot_spring_boot_starter.bot.SimpleWebhookBot;
import com.elessarov.telegram_bot_spring_boot_starter.properties.BotProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import static com.elessarov.telegram_bot_spring_boot_starter.constants.Constants.BOT_TYPE;
import static com.elessarov.telegram_bot_spring_boot_starter.constants.Constants.WEBHOOK_BOT_TYPE;

@Configuration
@EnableConfigurationProperties(BotProperties.class)
public class BotWebhookConfiguration {
    private final BotProperties botProperties;
    private static final Logger log = LoggerFactory.getLogger(BotWebhookConfiguration.class.getName());

    public BotWebhookConfiguration(BotProperties botProperties) {
        this.botProperties = botProperties;
    }

    @Bean
    @ConditionalOnProperty(name = BOT_TYPE, havingValue = WEBHOOK_BOT_TYPE)
    public SetWebhook setWebhook() {
        log.info("Setting webhook on {}", botProperties.getWebhookUrl());
        return SetWebhook.builder()
                .url(botProperties.getWebhookUrl())
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = BOT_TYPE, havingValue = WEBHOOK_BOT_TYPE)
    public SimpleWebhookBot webhookBot(
            BotProperties botProperties,
            SetWebhook setWebhook,
            UpdateHandler updateHandler
    ) {
        log.info("App started with webhook bot {}", botProperties.getName());
        return new SimpleWebhookBot(botProperties, setWebhook, updateHandler);
    }

    @Bean
    @ConditionalOnProperty(name = BOT_TYPE, havingValue = WEBHOOK_BOT_TYPE)
    public TelegramBotsApi telegramWebhookBotsApi(SpringWebhookBot webhookBot, SetWebhook setWebhook) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(webhookBot, setWebhook);
        return botsApi;
    }
}
