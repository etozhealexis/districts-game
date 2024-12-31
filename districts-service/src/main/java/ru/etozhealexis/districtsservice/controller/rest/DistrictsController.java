package ru.etozhealexis.districtsservice.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.etozhealexis.districtsservice.service.DistrictService;
import ru.etozhealexis.dto.DistrictAliasDto;
import ru.etozhealexis.dto.DistrictSettingDto;

@RestController
@RequestMapping("/district")
@RequiredArgsConstructor
@Slf4j
public class DistrictsController {
    private final DistrictService districtService;

    @PostMapping("/set")
    public void saveInvasion(@RequestBody DistrictSettingDto districtSettingDto) {
        log.info("Setting district to user {}", districtSettingDto.toString());
        districtService.setDistrictToUser(districtSettingDto);
    }

    @PostMapping("/alias")
    public void setAliasToDistrict(@RequestBody DistrictAliasDto districtAliasDto) {
        log.info("Setting alias to district {}", districtAliasDto.toString());
        districtService.setAliasToDistrict(districtAliasDto);
    }
}
