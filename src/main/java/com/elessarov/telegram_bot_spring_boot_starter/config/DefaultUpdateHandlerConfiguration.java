package com.elessarov.telegram_bot_spring_boot_starter.config;

import com.elessarov.telegram_bot_spring_boot_starter.bot.UpdateHandler;
import com.elessarov.telegram_bot_spring_boot_starter.util.BotUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.elessarov.telegram_bot_spring_boot_starter.constants.Constants.UPDATE_HANDLER_BEAN_MISSING;

@Configuration
public class DefaultUpdateHandlerConfiguration {
    private final Logger log = LoggerFactory.getLogger(DefaultUpdateHandlerConfiguration.class);

    @Bean
    @ConditionalOnMissingBean(UpdateHandler.class)
    public UpdateHandler defaultUpdateHandler() {
        return update -> {
            log.info(UPDATE_HANDLER_BEAN_MISSING);
            return new SendMessage(BotUtils.getChatId(update), UPDATE_HANDLER_BEAN_MISSING);
        };
    }
}
