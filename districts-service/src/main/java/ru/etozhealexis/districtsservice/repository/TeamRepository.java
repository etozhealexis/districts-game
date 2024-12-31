package ru.etozhealexis.districtsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.etozhealexis.districtsservice.model.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer>  {
}
