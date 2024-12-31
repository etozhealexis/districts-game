package ru.etozhealexis.telegrambotservice.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import ru.etozhealexis.dto.ActionDto;
import ru.etozhealexis.dto.ConfirmationDto;
import ru.etozhealexis.dto.ConfirmationResponseDto;
import ru.etozhealexis.dto.DistrictAliasDto;
import ru.etozhealexis.dto.DistrictSettingDto;
import ru.etozhealexis.telegrambotservice.feign.DistrictsFeign;

@Service
@RequiredArgsConstructor
@Slf4j
public class DistrictsMessagingService {
    private final DistrictsFeign districtsFeign;

    public void sendAction(ActionDto actionDto) {
        log.info("Sending action {}", actionDto.toString());
        districtsFeign.sendAction(actionDto);
    }

    public ConfirmationResponseDto confirmAction(ConfirmationDto confirmationDto) {
        log.info("Confirming action {}", confirmationDto.toString());
        return districtsFeign.confirmAction(confirmationDto);
    }

    public ConfirmationResponseDto declineAction(ConfirmationDto confirmationDto) {
        log.info("Declining action {}", confirmationDto.toString());
        return districtsFeign.declineAction(confirmationDto);
    }

    public void setDistrictToUser(DistrictSettingDto districtSettingDto) {
        log.info("Setting district {}", districtSettingDto.toString());
        districtsFeign.setDistrictToUser(districtSettingDto);
    }

    public void setAliasToDistrict(DistrictAliasDto districtAliasDto) {
        log.info("Setting alias {}", districtAliasDto.toString());
        districtsFeign.setAliasToDistrict(districtAliasDto);
    }
}
