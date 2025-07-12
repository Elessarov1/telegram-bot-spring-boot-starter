package com.elessarov.telegram_bot_spring_boot_starter.config;

import com.elessarov.telegram_bot_spring_boot_starter.bot.SimpleLongPollingBot;
import com.elessarov.telegram_bot_spring_boot_starter.bot.UpdateHandler;
import com.elessarov.telegram_bot_spring_boot_starter.properties.BotProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import static com.elessarov.telegram_bot_spring_boot_starter.constants.Constants.*;

@Configuration
@EnableConfigurationProperties(BotProperties.class)
@ConditionalOnProperty(name = WEBHOOK_ENABLED, havingValue = "false", matchIfMissing = true)
public class BotAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(BotAutoConfiguration.class);
    private final BotProperties botProperties;

    public BotAutoConfiguration(BotProperties botProperties) {
        this.botProperties = botProperties;
    }

    @Bean
    public SimpleLongPollingBot longPollingBot(UpdateHandler updateHandler) {
        log.info("App started with long polling bot {}", botProperties.getName());
        return new SimpleLongPollingBot(botProperties, updateHandler);
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(SimpleLongPollingBot bot) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);
        log.info("Bot is registered via TelegramBotsApi");
        return botsApi;
    }
}
