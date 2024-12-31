package ru.etozhealexis.districtsservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.etozhealexis.districtsservice.model.Participant;
import ru.etozhealexis.districtsservice.model.Team;
import ru.etozhealexis.districtsservice.repository.ParticipantRepository;
import ru.etozhealexis.districtsservice.repository.TeamRepository;
import ru.etozhealexis.dto.TeamDto;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamService {
    private final TeamRepository teamRepository;
    private final ParticipantRepository participantRepository;

    @Transactional
    public void createTeam(TeamDto teamDto) {
        log.info("Creating team.");
        List<Participant> participants = new ArrayList<>(teamDto.getUsernames().size());
        teamDto.getUsernames().stream()
                .map(participantRepository::getByUsername)
                .forEach(participant -> {
                    if (participant.hasTeam()) {
                        log.error("Participant {} already has a team", participant.getUsername());
                    } else {
                        participants.add(participant);
                    }
                });
        Team team = Team.builder()
                .name(teamDto.getName())
                .color(teamDto.getColor())
                .participants(participants)
                .build();
        teamRepository.save(team);
    }
}
