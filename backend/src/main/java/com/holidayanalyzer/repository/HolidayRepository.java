package com.holidayanalyzer.repository;

import com.holidayanalyzer.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    List<Holiday> findByCountryCodeAndYear(String countryCode, int year);

    @Query("""
        select h from Holiday h
        where h.countryCode = :countryCode
          and h.year = :year
          and (
            h.region is null
            or h.region.code = :regionCode
          )
        """)
    List<Holiday> findByCountryCodeYearAndRegion(
            @Param("countryCode") String countryCode,
            @Param("year") int year,
            @Param("regionCode") String regionCode
    );
}
