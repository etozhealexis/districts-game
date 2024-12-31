package ru.etozhealexis.telegrambotservice.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.etozhealexis.dto.ActionAttemptDto;
import ru.etozhealexis.dto.ValidationErrorResponseDto;
import ru.etozhealexis.telegrambotservice.bot.DistrictsBot;
import ru.etozhealexis.telegrambotservice.config.RefereeConfig;
import ru.etozhealexis.telegrambotservice.model.Action;
import ru.etozhealexis.telegrambotservice.repository.ActionRepository;
import ru.etozhealexis.telegrambotservice.util.Constants;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActionService {
    private final ActionRepository actionRepository;
    private final DistrictsBot districtsBot;

    @Value("${telegram.game-chat.id}")
    private Long gameChatId;

    @SneakyThrows
    @Transactional
    public void sendActionAttemptMessage(ActionAttemptDto actionAttemptDto) {
        Message message =
                switch (actionAttemptDto.getActionType()) {
                    case INVASION ->
                            districtsBot.sendConfirmationMessage(String.format(Constants.DISTRICT_INVADE_MESSAGE,
                                    actionAttemptDto.getParticipant().getUsername(), actionAttemptDto.getDistrict()));
                    case DEFENCE ->
                            districtsBot.sendConfirmationMessage(String.format(Constants.DISTRICT_DEFEND_MESSAGE,
                                    actionAttemptDto.getParticipant().getUsername(), actionAttemptDto.getDistrict()));
                };
        actionRepository.save(Action.builder()
                .messageId(message.getMessageId())
                .district(actionAttemptDto.getDistrict())
                .userId(actionAttemptDto.getParticipant().getUserId())
                .build());
    }

    public void sendActionValidationError(ValidationErrorResponseDto validationErrorResponseDto) {
        districtsBot.sendMessage(gameChatId,
                "@" + validationErrorResponseDto.getUsername() + " " + validationErrorResponseDto.getMessage(),
                null); // поменять на групповой чат
    }
}
