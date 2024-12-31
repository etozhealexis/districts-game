package ru.etozhealexis.telegrambotservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.etozhealexis.dto.DailyStatisticsDto;
import ru.etozhealexis.telegrambotservice.bot.DistrictsBot;
import ru.etozhealexis.telegrambotservice.model.DailyStatistics;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final DistrictsBot districtsBot;

    @Value("${telegram.game-chat.id}")
    private Long gameChatId;

    public void sendDailyStatistics(DailyStatisticsDto dailyStatisticsDto) {
        StringBuilder sb = new StringBuilder();
        List<DailyStatistics> statisticsSorted = dailyStatisticsDto.getStatistics()
                .entrySet()
                .stream()
                .map(e -> DailyStatistics.builder()
                        .username(e.getKey())
                        .count(e.getValue().size())
                        .build())
                .sorted(Comparator.comparingInt(DailyStatistics::getCount).reversed())
                .toList();
        statisticsSorted.forEach(ds ->
                sb.append("@").append(ds.getUsername()).append(" : ").append(ds.getCount()).append("\n"));
        districtsBot.sendMessage(gameChatId, sb.toString(), null);
    }
}
