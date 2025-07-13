# Telegram Bot Spring Boot Starter

This is a Spring Boot starter that simplifies integration of Telegram bots into your Spring Boot applications. The starter supports two types of bots:
- **Long Polling Bot** (default)
- **Webhook Bot**

## Features

- **Easy Configuration:** Automatically configure your bot using application properties.
- **Update Handling:** Delegate update processing by implementing the `UpdateHandler` interface.
- **Utility Methods (`BotUtils`):** Common Telegram API helpers for chat IDs, keyboards, commands, etc.
- **Default Update Handler:** A no‑op implementation to log and respond when no custom `UpdateHandler` is provided.

## Getting Started

### 1. Add the Dependency

#### Gradle
```groovy
dependencies {
    implementation 'com.github.Elessarov1:telegram-bot-spring-boot-starter:1.0.3'
}
```

If you're using **JitPack**, add its repository at the top of your `build.gradle`:
```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}
```

You can find your artifacts on JitPack at:

> https://jitpack.io/#elessarov/telegram-bot-spring-boot-starter

#### Maven
```xml
<dependency>
    <groupId>com.github.Elessarov1</groupId>
    <artifactId>telegram-bot-spring-boot-starter</artifactId>
    <version>1.0.3</version>
</dependency>
```

For JitPack with Maven, add to your `pom.xml`:
```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

### 2. Configuration Configuration

Define your Telegram bot properties in `application.yml` or `application.properties`:

```yaml
telegram:
  bot:
    name: YourBotName
    token: your_telegram_bot_token
    webhook:
      enabled: true          # set to false for long polling
      url: https://your-domain.com/telegram/webhook
``` 

### 3. Implement UpdateHandler

Create a Spring bean implementing `UpdateHandler` to handle incoming updates:

```java
@Component
public class MyUpdateHandler implements UpdateHandler {
    @Override
    public SendMessage handle(Update update) {
        if (isTextMessage(update)) {
            String text = update.getMessage().getText();
            String chatId = BotUtils.getChatId(update);
            return new SendMessage(chatId, "You said: '" + text + "'");
        }
        return null;
    }
}
```

If no `UpdateHandler` bean is defined, a default no‑op handler will log a warning and reply with a fixed message.

---

## Utility Methods (`BotUtils`)

Below are the available helper methods in `BotUtils`, along with a brief description and usage example for each.

### `getChatId(Update update)`
Retrieves the chat ID of a message update as a `String`.

```java
String chatId = BotUtils.getChatId(update);
```

### `getCallbackChatId(Update update)`
Retrieves the chat ID from a callback query update.

```java
String callbackChatId = BotUtils.getCallbackChatId(update);
```

### `getUserNameFromCallback(Update update)`
Gets the username of the user who triggered a callback query.

```java
String userName = BotUtils.getUserNameFromCallback(update);
```

### `addInlineKeyBoard(SendMessage sendMessage, List<Button> buttons, int buttonsPerRow)`
Adds an inline keyboard to the provided `SendMessage`, arranging buttons into rows of the specified size. Throws `RuntimeException` if any button text exceeds 30 characters.

```java
List<Button> buttons = List.of(
        new Button("Yes", "yes_cb"),
        new Button("No",  "no_cb")
);
BotUtils.addInlineKeyBoard(sendMessage, buttons, 2);
```

### `addKeyBoard(SendMessage sendMessage, List<String> labels, int buttonsPerRow)`
Adds a reply keyboard (custom markup) with text buttons arranged per row.

```java
List<String> keys = List.of("A", "B", "C");
BotUtils.addKeyBoard(sendMessage, keys, 2);
```

### `removeKeyboard(SendMessage sendMessage)`
Removes any visible keyboard by setting a `ReplyKeyboardRemove` markup.

```java
BotUtils.removeKeyboard(sendMessage);
```

### `setCommands(AbsSender bot, List<BotCommand> commands)`
Sets the bot’s command menu using the `SetMyCommands` Telegram API call. No action if `commands` is null or empty.

```java
List<BotCommand> commands = List.of(
        new BotCommand("start", "Start the bot"),
        new BotCommand("help",  "Show help message")
);
BotUtils.setCommands(bot, commands);
```

---

## License and Third-Party Notices

This project is licensed under the MIT License – see [LICENSE](LICENSE.md) for details.

**Third‑Party Dependency:** Uses [telegrambots-spring-boot-starter](https://github.com/rubenlagus/TelegramBots) v6.9.7.1 (MIT License).

---

## Contributing

Contributions are welcome! Fork the repo, create a feature branch, push your changes and open a pull request.

## Disclaimer

Provided "as is" without warranty. The authors are not liable for any damages arising from use.
