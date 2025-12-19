package com.holidayanalyzer.repository;

import com.holidayanalyzer.model.SchoolHoliday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SchoolHolidayRepository extends JpaRepository<SchoolHoliday, Long> {

    @Query("SELECT sh FROM SchoolHoliday sh WHERE sh.region.code = :regionCode AND sh.year = :year")
    List<SchoolHoliday> findByRegionCodeAndYear(@Param("regionCode") String regionCode, @Param("year") int year);

    @Query("SELECT sh FROM SchoolHoliday sh WHERE sh.region.code = :regionCode AND sh.startDate <= :endDate AND sh.endDate >= :startDate")
    List<SchoolHoliday> findByRegionCodeAndDateRange(@Param("regionCode") String regionCode, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT sh FROM SchoolHoliday sh WHERE sh.region.country.code = :countryCode AND sh.year = :year")
    List<SchoolHoliday> findByCountryCodeAndYear(@Param("countryCode") String countryCode, @Param("year") int year);
}
