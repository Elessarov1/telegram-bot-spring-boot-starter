package com.elessarov.telegram_bot_spring_boot_starter.config;

import com.elessarov.telegram_bot_spring_boot_starter.bot.UpdateHandler;
import com.elessarov.telegram_bot_spring_boot_starter.bot.SimpleWebhookBot;
import com.elessarov.telegram_bot_spring_boot_starter.controller.WebhookController;
import com.elessarov.telegram_bot_spring_boot_starter.properties.BotProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Objects;

import static com.elessarov.telegram_bot_spring_boot_starter.constants.Constants.WEBHOOK_ENABLED;

@Configuration
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@EnableConfigurationProperties(BotProperties.class)
@ConditionalOnProperty(name = WEBHOOK_ENABLED, havingValue = "true")
public class BotWebhookConfiguration {
    private static final Logger log = LoggerFactory.getLogger(BotWebhookConfiguration.class.getName());

    private final BotProperties botProperties;


    public BotWebhookConfiguration(BotProperties botProperties) {
        this.botProperties = botProperties;
    }

    @Bean
    public SetWebhook setWebhook() {
        log.info("Setting webhook on {}", botProperties.getWebhook().getUrl());
        var response = setWebhook(botProperties.getToken(), botProperties.getWebhook().getUrl());
        log.info("Response status - {}, description - {}, error_code - {}",
                Objects.requireNonNull(response.getBody()).ok(),
                response.getBody().description(),
                response.getBody().error_code()
        );
        return SetWebhook.builder()
                .url(botProperties.getWebhook().getUrl())
                .secretToken(botProperties.getToken())
                .build();
    }

    @Bean
    public SimpleWebhookBot webhookBot(
            BotProperties botProperties,
            SetWebhook setWebhook,
            UpdateHandler updateHandler
    ) {
        log.info("App started with webhook bot {}", botProperties.getName());
        return new SimpleWebhookBot(botProperties, setWebhook, updateHandler);
    }

    @Bean
    public TelegramBotsApi telegramWebhookBotsApi(SpringWebhookBot webhookBot, SetWebhook setWebhook) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(webhookBot, setWebhook);
        return botsApi;
    }

    @Bean
    public WebhookController webhookController(SimpleWebhookBot bot) {
        return new WebhookController(bot);
    }

    private static ResponseEntity<TelegramResponse> setWebhook(String token, String webhookUrl) {
        RestTemplate restTemplate = new RestTemplate();
        String endpoint = webhookUrl + BotProperties.Webhook.DEFAULT_PATH;
        String uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.telegram.org")
                .path("/bot{token}/setWebhook")
                .queryParam("url", endpoint)
                .buildAndExpand(token)
                .toUriString();

        return restTemplate.getForEntity(uri, TelegramResponse.class);
    }

    private record TelegramResponse(boolean ok, String description, Integer error_code){}
}
