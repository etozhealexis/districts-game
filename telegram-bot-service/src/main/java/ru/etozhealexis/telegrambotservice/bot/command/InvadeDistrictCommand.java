package ru.etozhealexis.telegrambotservice.bot.command;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.etozhealexis.dto.ActionDto;
import ru.etozhealexis.dto.ActionType;
import ru.etozhealexis.telegrambotservice.service.DistrictsMessagingService;
import ru.etozhealexis.telegrambotservice.util.Command;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class InvadeDistrictCommand extends DistrictsBotCommand {
    private final DistrictsMessagingService districtsMessagingService;

    @Value("${telegram.game-chat.id}")
    private Long gameChatId;

    protected InvadeDistrictCommand(DistrictsMessagingService districtsMessagingService) {
        super(Command.INVADE.getAlias(), Command.INVADE.getDescription());
        this.districtsMessagingService = districtsMessagingService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId());
        LocalDateTime invadeTime = LocalDateTime.now();

        if (chat.getId().equals(gameChatId)) {
            districtsMessagingService.sendAction(ActionDto.builder()
                    .messageText(Arrays.toString(arguments).replaceAll("[^а-яА-Я ]", "").trim())
                    .userId(user.getId())
                    .actionType(ActionType.INVASION)
                    .time(invadeTime)
                    .build());
        } else {
            message.setText("ты долбаеб");
        }

        execute(absSender, message, user);
    }
}
