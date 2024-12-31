package ru.etozhealexis.districtsservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.etozhealexis.districtsservice.feign.TelegramBotFeign;
import ru.etozhealexis.dto.ActionAttemptDto;
import ru.etozhealexis.dto.DailyStatisticsDto;
import ru.etozhealexis.dto.ValidationErrorResponseDto;

@Service
@RequiredArgsConstructor
public class TelegramBotMessagingService {
    private final TelegramBotFeign telegramBotFeign;

    public void sendActionAttempt(ActionAttemptDto actionAttemptDto) {
        telegramBotFeign.sendActionAttempt(actionAttemptDto);
    }

    public void sendDailyStatistics(DailyStatisticsDto dailyStatisticsDto) {
        telegramBotFeign.sendDailyStatistics(dailyStatisticsDto);
    }

    public void sendActionValidationError(ValidationErrorResponseDto validationErrorResponseDto) {
        telegramBotFeign.sendActionValidationError(validationErrorResponseDto);
    }
}
