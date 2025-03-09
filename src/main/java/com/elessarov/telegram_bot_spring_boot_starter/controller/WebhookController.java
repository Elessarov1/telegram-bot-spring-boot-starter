package com.elessarov.telegram_bot_spring_boot_starter.controller;

import com.elessarov.telegram_bot_spring_boot_starter.bot.SimpleWebhookBot;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.elessarov.telegram_bot_spring_boot_starter.constants.Constants.*;

@RestController
@RequestMapping("${telegram.bot.webhook-path}")
@ConditionalOnProperty(name = BOT_TYPE, havingValue = WEBHOOK_BOT_TYPE)
public class WebhookController {
    private final SimpleWebhookBot webhookBot;

    public WebhookController(SimpleWebhookBot webhookBot) {
        this.webhookBot = webhookBot;
    }

    @PostMapping
    public ResponseEntity<BotApiMethod<?>> onUpdateReceived(@RequestBody Update update) {
        BotApiMethod<?> response = webhookBot.onWebhookUpdateReceived(update);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to set webhook.
     * It sends a POST request to Telegram API using the bot token and webhook URL from BotProperties.
     * <p>
     * Example request:
     * PUT /setWebhook
     * <p>
     * Request Body: (none required)
     * <p>
     * Internally, it sends:
     * POST https://api.telegram.org/bot{botToken}/setWebhook
     * with Content-Type: application/x-www-form-urlencoded and body: url={webhookUrl}
     *
     * @return Telegram API response as a String.
     */
    @PutMapping
    public ResponseEntity<String> updateWebhook() {
        SetWebhook setWebhook = webhookBot.getSetWebhook();
        String token = setWebhook.getSecretToken();
        String webhookUrl = setWebhook.getUrl();
        String telegramApiUrl = SET_WEBHOOK_URL.formatted(token);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("url", webhookUrl);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(telegramApiUrl, requestEntity, String.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }


}
