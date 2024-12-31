package ru.etozhealexis.districtsservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.etozhealexis.districtsservice.model.Participant;
import ru.etozhealexis.districtsservice.repository.ParticipantRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParticipantService {
    private final ParticipantRepository participantRepository;

    @Transactional(readOnly = true)
    public Participant getById(Integer id) {
        return participantRepository.getReferenceById(id);
    }

    @Transactional(readOnly = true)
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Participant getByUserId(Long userId) {
        return participantRepository.getByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Participant getByUsername(String username) {
        return participantRepository.getByUsername(username);
    }
}
