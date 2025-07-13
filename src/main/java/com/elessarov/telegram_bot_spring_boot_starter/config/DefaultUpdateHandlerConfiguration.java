package com.elessarov.telegram_bot_spring_boot_starter.config;

import com.elessarov.telegram_bot_spring_boot_starter.bot.UpdateHandler;
import com.elessarov.telegram_bot_spring_boot_starter.util.BotUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Configuration
@ConditionalOnMissingBean(UpdateHandler.class)
public class DefaultUpdateHandlerConfiguration implements InitializingBean {
    public static final String UPDATE_HANDLER_BEAN_MISSING = "Update received but your UpdateHandler bean is not configured";

    private final Logger log = LoggerFactory.getLogger(DefaultUpdateHandlerConfiguration.class);

    @Bean
    public UpdateHandler noOpUpdateHandler() {
        return update -> {
            log.warn(UPDATE_HANDLER_BEAN_MISSING);
            return new SendMessage(BotUtils.getChatId(update), UPDATE_HANDLER_BEAN_MISSING);
        };
    }

    @Override
    public void afterPropertiesSet() {
        log.warn("UpdateHandler bean was not configured, noOpUpdateHandler will be used");
    }
}
