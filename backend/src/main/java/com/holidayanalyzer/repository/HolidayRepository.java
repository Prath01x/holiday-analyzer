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
            h.globalHoliday = true
            or (h.subdivisionCodes is not null and h.subdivisionCodes like concat('%', :subdivision, '%'))
          )
        """)
    List<Holiday> findByCountryCodeYearAndSubdivision(
            @Param("countryCode") String countryCode,
            @Param("year") int year,
            @Param("subdivision") String subdivision
    );
}
