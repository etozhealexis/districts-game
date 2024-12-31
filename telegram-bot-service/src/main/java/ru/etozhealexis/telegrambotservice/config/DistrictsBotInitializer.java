package ru.etozhealexis.telegrambotservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.etozhealexis.telegrambotservice.bot.DistrictsBot;

@Component
@RequiredArgsConstructor
@Slf4j
public class DistrictsBotInitializer {
    private final DistrictsBot districtsBot;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(districtsBot);
        } catch (TelegramApiException e) {
            log.error("sth went wrong " + e.getMessage());
        }
    }
}
