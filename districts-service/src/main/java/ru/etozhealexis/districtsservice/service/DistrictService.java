package ru.etozhealexis.districtsservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.etozhealexis.districtsservice.model.District;
import ru.etozhealexis.districtsservice.model.DistrictAlias;
import ru.etozhealexis.districtsservice.model.Participant;
import ru.etozhealexis.districtsservice.repository.DistrictRepository;
import ru.etozhealexis.dto.DailyStatisticsDto;
import ru.etozhealexis.dto.DistrictAliasDto;
import ru.etozhealexis.dto.DistrictDto;
import ru.etozhealexis.dto.DistrictSettingDto;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class DistrictService {
    private final DistrictRepository districtRepository;
    private final ParticipantService participantService;
    private final TelegramBotMessagingService telegramBotMessagingService;

    @Transactional
    public void setDistrictToUser(DistrictSettingDto districtSettingDto) {
        Participant participant = participantService.getByUsername(districtSettingDto.getUsername());
        District district = districtRepository.getByNameOrAlias(districtSettingDto.getDistrict());
        if (district != null) {
            district.setParticipant(participant);
        } else {
            log.error("District not found");
        }
    }

    @Transactional
    public void setAliasToDistrict(DistrictAliasDto districtAliasDto) {
        District district = districtRepository.getByNameOrAlias(districtAliasDto.getName());
        Set<DistrictAlias> districtAliases = district.getDistrictAlias();
        districtAliases.add(DistrictAlias.builder()
                .alias(districtAliasDto.getAlias())
                .district(district)
                .build());
        district.setDistrictAlias(districtAliases);
    }

    @Scheduled(cron = "0 30 * * * *")
    @Transactional
    public void reassignDistrictsOwners() {
        List<District> districts = districtRepository.findAll();
        for (District district : districts) {
            if (district.getInvader() != null && !district.isDefended()) {
                district.setParticipant(district.getInvader());
                district.setInvader(null);
                districtRepository.save(district);
            }
            district.setDefended(false);
        }
    }

    @Scheduled(cron = "3 30 * * * *")
    @Transactional(readOnly = true)
    public void sendDailyStatistics() {
        List<Participant> participants = participantService.getAllParticipants();
        HashMap<String, List<DistrictDto>> statistics = new HashMap<>(participants.size());
        participants.forEach(participant -> {
            List<DistrictDto> participantDistricts = districtRepository.getAllByParticipant(participant).stream()
                    .map(district -> DistrictDto.builder()
                            .name(district.getName())
                            .build())
                    .toList();
            if (!CollectionUtils.isEmpty(participantDistricts)) {
                statistics.put(participant.getUsername(), participantDistricts);
            }
        });
        if (!statistics.isEmpty()) {
            telegramBotMessagingService.sendDailyStatistics(DailyStatisticsDto.builder()
                    .statistics(statistics)
                    .build());
        }
    }
}
