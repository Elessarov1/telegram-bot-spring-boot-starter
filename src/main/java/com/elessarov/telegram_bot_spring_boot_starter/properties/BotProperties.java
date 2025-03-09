package com.elessarov.telegram_bot_spring_boot_starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.elessarov.telegram_bot_spring_boot_starter.constants.Constants.LONG_POLLING_BOT_TYPE;

@ConfigurationProperties("telegram.bot")
public class BotProperties {
    private String type = LONG_POLLING_BOT_TYPE;
    private String name;
    private String token;
    private String webhookPath;
    private String webhookUrl;

    public BotProperties() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getWebhookPath() {
        return webhookPath;
    }

    public void setWebhookPath(String webhookPath) {
        this.webhookPath = webhookPath;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }
}
