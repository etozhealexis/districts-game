package ru.etozhealexis.telegrambotservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.etozhealexis.dto.ConfirmationDto;
import ru.etozhealexis.dto.ConfirmationResponseDto;
import ru.etozhealexis.dto.DistrictAliasDto;
import ru.etozhealexis.dto.DistrictSettingDto;
import ru.etozhealexis.dto.ActionDto;

@FeignClient(value = "districts-service", url = "${integration.internal.host.districts-service}")
public interface DistrictsFeign {

    @PostMapping("/action/")
    void sendAction(ActionDto actionDto);

    @PutMapping("/action/confirm")
    ConfirmationResponseDto confirmAction(ConfirmationDto confirmationDto);

    @PutMapping("/action/decline")
    ConfirmationResponseDto declineAction(ConfirmationDto confirmationDto);

    @PostMapping("/district/set")
    void setDistrictToUser(DistrictSettingDto districtSettingDto);

    @PostMapping("/district/alias")
    void setAliasToDistrict(DistrictAliasDto districtAliasDto);
}
