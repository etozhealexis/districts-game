package ru.etozhealexis.districtsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.etozhealexis.districtsservice.model.Participant;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {

    Participant getByUserId(Long userId);

    Participant getByUsername(String username);
}
