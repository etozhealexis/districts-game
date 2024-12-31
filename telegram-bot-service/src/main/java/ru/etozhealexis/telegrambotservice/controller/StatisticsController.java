package ru.etozhealexis.telegrambotservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.etozhealexis.dto.DailyStatisticsDto;
import ru.etozhealexis.telegrambotservice.service.StatisticsService;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @PostMapping(value = "/daily", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void sendDailyStatistics(@RequestBody DailyStatisticsDto dailyStatisticsDto) {
        statisticsService.sendDailyStatistics(dailyStatisticsDto);
    }
}
