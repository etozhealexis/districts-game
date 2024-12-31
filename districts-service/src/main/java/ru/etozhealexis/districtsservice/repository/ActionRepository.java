package ru.etozhealexis.districtsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.etozhealexis.districtsservice.model.Action;
import ru.etozhealexis.districtsservice.model.District;
import ru.etozhealexis.districtsservice.model.Participant;

import java.util.Optional;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {

    @Query("SELECT a FROM Action a WHERE a.participant = :participant AND a.district = :district AND a.actionStatus = 'IN_PROGRESS'")
    Optional<Action> findInProgressAction(Participant participant, District district);

    @Query("SELECT a FROM Action a WHERE a.participant = :participant " +
            "AND a.actionStatus= 'COMPLETED' AND a.actionType = 'INVASION' " +
            "ORDER BY a.time DESC LIMIT 1")
    Optional<Action> findLastInProgressAction(Participant participant);
}
