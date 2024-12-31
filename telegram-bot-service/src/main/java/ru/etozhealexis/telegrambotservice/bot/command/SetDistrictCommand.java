package ru.etozhealexis.telegrambotservice.bot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.etozhealexis.dto.DistrictSettingDto;
import ru.etozhealexis.telegrambotservice.config.RefereeConfig;
import ru.etozhealexis.telegrambotservice.service.DistrictsMessagingService;
import ru.etozhealexis.telegrambotservice.util.Command;

import java.util.Arrays;

@Component
public class SetDistrictCommand extends DistrictsBotCommand {
    private final RefereeConfig refereeConfig;
    private final DistrictsMessagingService districtsMessagingService;

    protected SetDistrictCommand(RefereeConfig refereeConfig,
                                 DistrictsMessagingService districtsMessagingService) {
        super(Command.SET_DISTRICT.getAlias(), Command.SET_DISTRICT.getDescription());
        this.refereeConfig = refereeConfig;
        this.districtsMessagingService = districtsMessagingService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId());

        if (user.getId().equals(refereeConfig.getId())) {
            String username = arguments[arguments.length - 1].replace("@", "");
            String commandMessage = Arrays.toString(arguments);
            String district = commandMessage.replaceAll(username, "")
                    .replaceAll("[^а-яА-Я ]","")
                    .trim();
            message.setText("Район " + district + " присвоен " + username);
            districtsMessagingService.setDistrictToUser(
                    DistrictSettingDto.builder()
                            .district(district)
                            .username(username)
                            .build()
            );
        } else {
            message.setText("ты долбаеб");
        }

        execute(absSender, message, user);
    }
}
