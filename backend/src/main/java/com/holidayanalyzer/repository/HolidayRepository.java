package com.holidayanalyzer.repository;

import com.holidayanalyzer.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    List<Holiday> findByCountryCodeAndYear(String countryCode, int year);
}
