package com.holidayanalyzer.repository;

import com.holidayanalyzer.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    Optional<Region> findByCode(String code);

    @Query("SELECT r FROM Region r WHERE r.country.code = :countryCode")
    List<Region> findByCountryCode(@Param("countryCode") String countryCode);
}
