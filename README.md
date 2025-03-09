# Telegram Bot Spring Boot Starter

This is a Spring Boot starter that simplifies the integration of Telegram bots into your Spring Boot applications. The starter supports two types of bots:
- **Long Polling Bot** (default)
- **Webhook Bot**

## Features

- **Easy Configuration:** Automatically configure your bot using application properties.
- **Update Handling:** Delegate update processing by implementing the `UpdateHandler` interface.
- **Utility Methods:** Use built-in utility methods (in `BotUtils`) for common Telegram API tasks like adding keyboards, retrieving chat IDs, etc.

## Getting Started

### 1. Add the Dependency

#### Gradle
```groovy
dependencies {
    implementation 'com.elessarov:telegram-bot-spring-boot-starter:1.0.0'
    // For webhook functionality, ensure you include the web dependency in your project:
    implementation 'org.springframework.boot:spring-boot-starter-web'
}
```

#### Maven 
```maven
<dependency>
    <groupId>com.elessarov</groupId>
    <artifactId>telegram-bot-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
<!-- For webhook functionality, add this dependency to your project -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### 2. Configuration
- **Define your Telegram bot properties in your application.yml (or application.properties):**
```yaml
telegram:
  bot:
    type: LongPolling # or Webhook
    name: YourBotName
    token: your_telegram_bot_token
    webhook-path: /webhook
    webhook-url: https://your-public-url/webhook

```

### 3. Customizing Update Handling
- **To implement your business logic, simply create a bean that implements the UpdateHandler interface:**
```java
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MyUpdateHandler implements UpdateHandler {
    @Override
    public SendMessage handle(Update update) {
        SendMessage message = new SendMessage();
        // For example, echo back the received text:
        if (update.hasMessage() && update.getMessage().hasText()) {
            message.setChatId(String.valueOf(update.getMessage().getChatId()));
            message.setText("Echo: " + update.getMessage().getText());
        }
        return message;
    }
}
```
If you do not provide an UpdateHandler bean, a default one will be used that logs the update but performs no further action.

### 4. Utility Methods
The starter provides a set of utility methods in the BotUtils class for common operations, such as:
* Retrieving chat IDs.
* Adding inline or reply keyboards.
* Removing keyboards from messages.
* etc

For example, to add an inline keyboard with a custom number of buttons per row:
```java
List<Button> buttons = Arrays.asList(
    new Button("Button1", "callbackData1"),
    new Button("Button2", "callbackData2"),
    new Button("Button3", "callbackData3")
);

SendMessage sendMessage = new SendMessage();
sendMessage.setText("Choose an option");
BotUtils.addInlineKeyBoard(sendMessage, buttons, 2);
```

### 5. License and Third-Party Notices
This project is licensed under the MIT License – see the [LICENSE](LICENSE.md) file for details.

Third-Party Dependency:

This starter uses [telegrambots-spring-boot-starter](https://github.com/rubenlagus/TelegramBots)  version **6.9.7.1**, which is licensed under the MIT License. The original license and copyright:
```
MIT License

Copyright (c) 2016 Ruben Bermudez

Permission is hereby granted, free of charge, to any person obtaining a copy of this software...
```
For full details, please refer to the dependency's repository.

## Contributing
Contributions are welcome! Please follow standard GitHub practices (fork, pull request, etc.) for contributing to this project.

## Disclaimer
This project is provided "as is", without warranty of any kind. The authors are not liable for any damages or issues arising from the use of this software.