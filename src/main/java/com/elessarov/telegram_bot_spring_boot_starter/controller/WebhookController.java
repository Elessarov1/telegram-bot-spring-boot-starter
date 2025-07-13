package com.elessarov.telegram_bot_spring_boot_starter.controller;

import com.elessarov.telegram_bot_spring_boot_starter.bot.SimpleWebhookBot;
import com.elessarov.telegram_bot_spring_boot_starter.properties.BotProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequestMapping(BotProperties.Webhook.DEFAULT_PATH)
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
}
