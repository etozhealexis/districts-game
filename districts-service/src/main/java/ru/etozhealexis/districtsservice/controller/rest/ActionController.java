package ru.etozhealexis.districtsservice.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.etozhealexis.districtsservice.service.ActionService;
import ru.etozhealexis.dto.ActionDto;
import ru.etozhealexis.dto.ConfirmationDto;
import ru.etozhealexis.dto.ConfirmationResponseDto;

@RestController
@RequestMapping("/action")
@RequiredArgsConstructor
@Slf4j
public class ActionController {
    private final ActionService actionService;

    @PostMapping("/")
    public void saveAction(@RequestBody ActionDto actionDto) {
        log.info("Save action {}", actionDto.toString());
        actionService.saveAction(actionDto);
    }

    @PutMapping("/confirm")
    public ConfirmationResponseDto confirmAction(@RequestBody ConfirmationDto confirmationDto) {
        log.info("Confirm action {}", confirmationDto.toString());
        return actionService.confirmAction(confirmationDto);
    }

    @PutMapping("/decline")
    public ConfirmationResponseDto declineAction(@RequestBody ConfirmationDto confirmationDto) {
        log.info("Decline action {}", confirmationDto.toString());
        return actionService.declineAction(confirmationDto);
    }
}
