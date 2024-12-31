package ru.etozhealexis.telegrambotservice.bot;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.etozhealexis.dto.ActionDto;
import ru.etozhealexis.dto.ActionType;
import ru.etozhealexis.dto.ConfirmationDto;
import ru.etozhealexis.dto.ConfirmationResponseDto;
import ru.etozhealexis.telegrambotservice.bot.command.DistrictsBotCommand;
import ru.etozhealexis.telegrambotservice.config.DistrictsBotConfig;
import ru.etozhealexis.telegrambotservice.config.RefereeConfig;
import ru.etozhealexis.telegrambotservice.model.Action;
import ru.etozhealexis.telegrambotservice.repository.ActionRepository;
import ru.etozhealexis.telegrambotservice.service.DistrictsMessagingService;
import ru.etozhealexis.telegrambotservice.util.Constants;
import ru.etozhealexis.telegrambotservice.util.InlineKeyboardCallback;
import ru.etozhealexis.telegrambotservice.util.InlineKeyboardMarkupUtil;
import ru.etozhealexis.telegrambotservice.util.StatusMailing;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class DistrictsBot extends TelegramLongPollingCommandBot {
    private final DistrictsBotConfig districtsBotConfig;
    private final RefereeConfig refereeConfig;
    private final DistrictsMessagingService districtsMessagingService;
    private final InlineKeyboardMarkupUtil inlineKeyboardMarkupUtil;
    private final ActionRepository actionRepository;

    @Value("${telegram.game-chat.id}")
    private Long gameChatId;

    @Autowired
    public DistrictsBot(
            DistrictsBotConfig districtsBotConfig,
            RefereeConfig refereeConfig,
            DistrictsMessagingService districtsMessagingService,
            InlineKeyboardMarkupUtil inlineKeyboardMarkupUtil,
            ActionRepository actionRepository,
            List<DistrictsBotCommand> commands) {
        super();
        this.districtsBotConfig = districtsBotConfig;
        this.refereeConfig = refereeConfig;
        this.districtsMessagingService = districtsMessagingService;
        this.inlineKeyboardMarkupUtil = inlineKeyboardMarkupUtil;
        this.actionRepository = actionRepository;
        commands.forEach(this::register);
    }

    @Override
    public String getBotUsername() {
        return districtsBotConfig.getName();
    }

    @Override
    public String getBotToken() {
        return districtsBotConfig.getToken();
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            if (chatId.equals(gameChatId)) {
                processGroupChatMessage(message);
            } else if (chatId.equals(refereeConfig.getId())) {
                processRefereePrivateMessage(message);
            } else {
                processPrivateMessage(message);
            }
        }
        if (update.hasCallbackQuery()) {
            processCallbackQuery(update);
        }
    }

    private void processCallbackQuery(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Integer messageToDeleteId = callbackQuery.getMessage().getMessageId();
        Action action = actionRepository.getByMessageId(messageToDeleteId);

        ConfirmationDto confirmationDto = ConfirmationDto.builder()
                .districtName(action.getDistrict())
                .userId(action.getUserId())
                .build();
        if (callbackQuery.getData().equals(InlineKeyboardCallback.CONFIRM_ACTION.getData())) {
            ConfirmationResponseDto confirmationResponse = districtsMessagingService.confirmAction(confirmationDto);
            sendMessage(refereeConfig.getId(), "Действие подтвержено", null);
            String messageText;
            if (confirmationResponse.getInvadedUsername() != null) {
                messageText = "@" + confirmationResponse.getUsername() + " пишу" +
                        "\n@" + confirmationResponse.getInvadedUsername() + " захвачен район " + action.getDistrict();
            } else {
                messageText = "@" + confirmationResponse.getUsername() + " пишу";
            }
            sendMessage(gameChatId, messageText, confirmationResponse.getMessageId());
        } else if (callbackQuery.getData().equals(InlineKeyboardCallback.DECLINE_ACTION.getData())) {
            ConfirmationResponseDto confirmationResponse = districtsMessagingService.declineAction(confirmationDto);
            sendMessage(refereeConfig.getId(), "Действие отклонено", null);
            sendMessage(gameChatId, "@" + confirmationResponse.getUsername() + " не подтверждено", confirmationResponse.getMessageId());
        }

        deleteMessageMarkup(messageToDeleteId);
    }

    public Message sendConfirmationMessage(String text) throws TelegramApiException {
        SendMessage message = new SendMessage(refereeConfig.getId().toString(), text);
        message.setReplyMarkup(inlineKeyboardMarkupUtil.generateMergeRequestMarkup());
        return execute(message);
    }

    @SneakyThrows
    private void deleteMessageMarkup(Integer messageId) {
        EditMessageReplyMarkup message = new EditMessageReplyMarkup();
        message.setChatId(refereeConfig.getId().toString());
        message.setMessageId(messageId);
        message.setReplyMarkup(null);
        execute(message);
    }

    private void processGroupChatMessage(Message message) {
        Long userId = message.getFrom().getId();
        String messageText = message.getText();
        Integer messageId = message.getMessageId();
        LocalDateTime messageTime = LocalDateTime.now();

        ActionDto.ActionDtoBuilder actionDtoBuilder = ActionDto.builder()
                .userId(userId)
                .messageText(messageText)
                .messageId(messageId)
                .time(messageTime);
        if (messageText.startsWith(
                String.format(Constants.DISTRICT_INVADE_TRIGGER_MESSAGE_PART, refereeConfig.getUsername()))) {
            districtsMessagingService.sendAction(actionDtoBuilder
                    .actionType(ActionType.INVASION)
                    .build());
        }
        if (messageText.startsWith(
                String.format(Constants.DISTRICT_DEFEND_TRIGGER_MESSAGE_PART, refereeConfig.getUsername()))) {
            districtsMessagingService.sendAction(actionDtoBuilder
                    .actionType(ActionType.DEFENCE)
                    .build());
        }
    }

    @SneakyThrows
    private void processRefereePrivateMessage(Message message) {
//        do nothing
        processGroupChatMessage(message);
    }

    private void processPrivateMessage(Message message) {
        sendMessage(message.getChatId(), "не пиши сюда, дебил", null);
    }

    public StatusMailing sendMessage(Long chatId, String messageText, Integer replyMessageId) {
        SendMessage message = new SendMessage(chatId.toString(), messageText);
        if (replyMessageId != null) {
            message.setReplyToMessageId(replyMessageId);
        }
        try {
            execute(message);
            return StatusMailing.SUCCESS;
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            return StatusMailing.ERROR;
        }
    }

    public StatusMailing forwardMessage(Long fromChatId, Long chatId, Integer messageId) {
        ForwardMessage forwardMessage = ForwardMessage.builder()
                .fromChatId(fromChatId)
                .chatId(chatId)
                .messageId(messageId)
                .build();
        try {
            execute(forwardMessage);
            return StatusMailing.SUCCESS;
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            return StatusMailing.ERROR;
        }
    }
}
