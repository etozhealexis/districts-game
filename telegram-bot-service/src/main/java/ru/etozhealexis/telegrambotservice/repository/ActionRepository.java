package ru.etozhealexis.telegrambotservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.etozhealexis.telegrambotservice.model.Action;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {

    Action getByMessageId(Integer messageId);
}
