package ru.etozhealexis.telegrambotservice.bot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.etozhealexis.dto.DistrictAliasDto;
import ru.etozhealexis.telegrambotservice.config.RefereeConfig;
import ru.etozhealexis.telegrambotservice.service.DistrictsMessagingService;
import ru.etozhealexis.telegrambotservice.util.Command;

@Component
public class SetDistrictAliasCommand extends DistrictsBotCommand {
    private final RefereeConfig refereeConfig;
    private final DistrictsMessagingService districtsMessagingService;

    protected SetDistrictAliasCommand(RefereeConfig refereeConfig,
                                 DistrictsMessagingService districtsMessagingService) {
        super(Command.SET_DISTRICT_ALIAS.getAlias(), Command.SET_DISTRICT_ALIAS.getDescription());
        this.refereeConfig = refereeConfig;
        this.districtsMessagingService = districtsMessagingService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId());

        if (user.getId().equals(refereeConfig.getId())) {
            StringBuilder district = new StringBuilder();
            StringBuilder districtAlias = new StringBuilder();
            boolean isAlias = false;
            for (String argument : arguments) {
                if (argument.contains("тег:")) {
                    isAlias = true;
                    continue;
                }
                if (isAlias) {
                    districtAlias.append(argument).append(" ");
                } else {
                    district.append(argument).append(" ");
                }
            }

            districtsMessagingService.setAliasToDistrict(
                    DistrictAliasDto.builder()
                            .name(district.toString().trim())
                            .alias(districtAlias.toString().trim())
                            .build()
            );
        } else {
            message.setText("ты долбаеб");
        }

        execute(absSender, message, user);
    }
}
