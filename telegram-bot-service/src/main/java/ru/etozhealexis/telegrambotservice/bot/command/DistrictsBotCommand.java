package ru.etozhealexis.telegrambotservice.bot.command;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public abstract class DistrictsBotCommand extends BotCommand {

    protected DistrictsBotCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    public void execute(AbsSender sender, SendMessage message, User user) {
        log.info(this.getCommandIdentifier() + " , пользователь - " + user.getUserName() + ", id - " + user.getId());
        log.info("output: " + message.getText());
        try {
            sender.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
