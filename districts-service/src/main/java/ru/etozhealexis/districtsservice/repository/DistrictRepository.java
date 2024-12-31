package ru.etozhealexis.districtsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.etozhealexis.districtsservice.model.District;
import ru.etozhealexis.districtsservice.model.Participant;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {

    @Query(value = "SELECT d FROM District d " +
            "WHERE LOWER(d.name) = LOWER(:districtName) OR LOWER(:districtName) IN " +
            "(SELECT LOWER(da.alias) FROM DistrictAlias da WHERE d = da.district)")
    District getByNameOrAlias(@Param("districtName") String name);

    List<District> getAllByParticipant(Participant participant);
}
