package ru.etozhealexis.telegrambotservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.etozhealexis.dto.ActionAttemptDto;
import ru.etozhealexis.dto.ValidationErrorResponseDto;
import ru.etozhealexis.telegrambotservice.service.ActionService;

@RestController
@RequestMapping("/action")
@RequiredArgsConstructor
public class ActionController {
    private final ActionService actionService;

    @PostMapping("/")
    public void sendActionAttemptMessage(@RequestBody ActionAttemptDto actionAttemptDto) {
        actionService.sendActionAttemptMessage(actionAttemptDto);
    }

    @PostMapping(value = "/validation-error")
    public void sendActionValidationError(@RequestBody ValidationErrorResponseDto validationErrorResponseDto) {
        actionService.sendActionValidationError(validationErrorResponseDto);
    }
}
