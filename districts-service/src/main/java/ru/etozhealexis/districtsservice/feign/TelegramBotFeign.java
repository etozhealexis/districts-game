package ru.etozhealexis.districtsservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.etozhealexis.dto.ActionAttemptDto;
import ru.etozhealexis.dto.DailyStatisticsDto;
import ru.etozhealexis.dto.ValidationErrorResponseDto;

@FeignClient(name="TelegramBotApiFeign", url="${integration.internal.host.telegram-bot-service}")
public interface TelegramBotFeign {

    @PostMapping(value = "/action/")
    void sendActionAttempt(@RequestBody ActionAttemptDto actionAttemptDto);

    @PostMapping(value = "/action/validation-error")
    void sendActionValidationError(@RequestBody ValidationErrorResponseDto validationErrorResponseDto);

    @PostMapping(value = "/statistics/daily", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    void sendDailyStatistics(@RequestBody DailyStatisticsDto dailyStatisticsDto);
}
