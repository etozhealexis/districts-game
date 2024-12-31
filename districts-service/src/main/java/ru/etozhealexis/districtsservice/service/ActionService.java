package ru.etozhealexis.districtsservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.etozhealexis.districtsservice.model.Action;
import ru.etozhealexis.districtsservice.model.ActionStatus;
import ru.etozhealexis.districtsservice.model.District;
import ru.etozhealexis.districtsservice.model.Participant;
import ru.etozhealexis.districtsservice.repository.ActionRepository;
import ru.etozhealexis.districtsservice.repository.DistrictRepository;
import ru.etozhealexis.dto.ActionAttemptDto;
import ru.etozhealexis.dto.ActionDto;
import ru.etozhealexis.dto.ActionType;
import ru.etozhealexis.dto.ConfirmationDto;
import ru.etozhealexis.dto.ConfirmationResponseDto;
import ru.etozhealexis.dto.ParticipantDto;
import ru.etozhealexis.dto.ValidationErrorResponseDto;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActionService {
    private final ActionRepository actionRepository;
    private final DistrictRepository districtRepository;
    private final ParticipantService participantService;
    private final TelegramBotMessagingService telegramBotMessagingService;

    @Transactional
    public void saveAction(ActionDto actionDto) {
        Participant participant = participantService.getByUserId(actionDto.getUserId());
        String districtName = actionDto.getMessageText()
                .replaceAll("@[\\w\\d_]+", "")
                .replace("захватываю", "")
                .replace("дефаю", "")
                .trim();
        District district = districtRepository.getByNameOrAlias(districtName);
        if (!validateAction(participant, district, actionDto)) {
            return;
        }
        ActionType actionType = actionDto.getActionType();
        Action invasion = Action.builder()
                .messageText(actionDto.getMessageText())
                .messageId(actionDto.getMessageId())
                .district(district)
                .participant(participant)
                .time(actionDto.getTime())
                .actionType(actionType)
                .actionStatus(ActionStatus.IN_PROGRESS)
                .build();
        actionRepository.save(invasion);
        telegramBotMessagingService.sendActionAttempt(ActionAttemptDto.builder()
                .participant(ParticipantDto.builder()
                        .userId(participant.getUserId())
                        .username(participant.getUsername())
                        .build())
                .district(districtName)
                .actionType(actionType)
                .build());
    }

    @Transactional
    public ConfirmationResponseDto confirmAction(ConfirmationDto confirmationDto) {
        Participant participant = participantService.getByUserId(confirmationDto.getUserId());
        District district = districtRepository.getByNameOrAlias(confirmationDto.getDistrictName());
        Optional<Action> actionOpt = actionRepository.findInProgressAction(participant, district);
        if (actionOpt.isEmpty()) {
            log.error("Action not found");
            throw new IllegalStateException();
        }
        Action action = actionOpt.get();
        action.setActionStatus(ActionStatus.COMPLETED);
        switch (action.getActionType()) {
            case INVASION -> district.setInvader(participant);
            case DEFENCE -> district.setDefended(true);
        }
        district.setInvader(participant);
        return ConfirmationResponseDto.builder()
                .username(action.getParticipant().getUsername())
                .invadedUsername(action.getActionType().equals(ActionType.INVASION) ? Optional.ofNullable(district.getParticipant())
                        .map(Participant::getUsername)
                        .orElse(null) : null)
                .messageId(action.getMessageId())
                .build();
    }

    @Transactional
    public ConfirmationResponseDto declineAction(ConfirmationDto confirmationDto) {
        Participant participant = participantService.getByUserId(confirmationDto.getUserId());
        District district = districtRepository.getByNameOrAlias(confirmationDto.getDistrictName());
        Optional<Action> actionOpt = actionRepository.findInProgressAction(participant, district);
        if (actionOpt.isEmpty()) {
            log.error("Action not found");
            throw new IllegalStateException();
        }
        Action action = actionOpt.get();
        action.setActionStatus(ActionStatus.DECLINED);
        return ConfirmationResponseDto.builder()
                .username(action.getParticipant().getUsername())
                .messageId(action.getMessageId())
                .build();
    }

    private boolean validateAction(Participant participant, District district, ActionDto actionDto) {
        if (checkForAlreadyDefended(participant, district)) return false;
        if (checkForExistingInvader(participant, district, actionDto)) return false;
        if (checkForInProgressAction(participant, district)) return false;
        return !checkForTime(participant, actionDto);
    }

    private boolean checkForAlreadyDefended(Participant participant, District district) {
        if (district.isDefended()) {
            log.warn("District already defended");
            telegramBotMessagingService.sendActionValidationError(ValidationErrorResponseDto.builder()
                    .message("район " + district.getName() + " уже задефан")
                    .username(participant.getUsername())
                    .build());
            return true;
        }
        return false;
    }

    private boolean checkForTime(Participant participant, ActionDto actionDto) {
        Optional<Action> invadeActionOpt = actionRepository.findLastInProgressAction(participant);
        if (actionDto.getActionType().equals(ActionType.INVASION) && invadeActionOpt.isPresent()) {
            Action invadeAction = invadeActionOpt.get();
            if (!invadeAction.getTime().plusMinutes(10).isBefore(actionDto.getTime())) {
                log.warn("Invasion done in 10 minute gap {}", actionDto);
                telegramBotMessagingService.sendActionValidationError(ValidationErrorResponseDto.builder()
                        .message("Прошло меньше 10 минут с последнего захвата")
                        .username(participant.getUsername())
                        .build());
                return true;
            }
        }
        return false;
    }

    private boolean checkForInProgressAction(Participant participant, District district) {
        Optional<Action> inProgressActionOpt = actionRepository.findInProgressAction(participant, district);
        if (inProgressActionOpt.isPresent()) {
            log.warn("Action already in progress {}", inProgressActionOpt.get());
            telegramBotMessagingService.sendActionValidationError(ValidationErrorResponseDto.builder()
                    .message("действие по району " + district.getName() + " уже и так имеется")
                    .username(participant.getUsername())
                    .build());
            return true;
        }
        return false;
    }

    private boolean checkForExistingInvader(Participant participant, District district, ActionDto actionDto) {
        if (actionDto.getActionType().equals(ActionType.INVASION) && district.getInvader() != null) {
            log.warn("District already has an invader");
            String messageText = "";
            if (actionDto.getUserId().equals(district.getInvader().getUserId())) {
                messageText = "район " + district.getName() + " уже и так под твоей атакой, додик";
            }
            if (actionDto.getUserId().equals(district.getParticipant().getUserId())) {
                messageText = "район " + district.getName() + " и так твой, долбаебина";
            }
            telegramBotMessagingService.sendActionValidationError(ValidationErrorResponseDto.builder()
                    .message(messageText)
                    .username(participant.getUsername())
                    .build());
            return true;
        }
        return false;
    }
}
