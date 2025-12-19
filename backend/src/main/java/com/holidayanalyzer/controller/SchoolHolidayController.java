package com.holidayanalyzer.controller;

import com.holidayanalyzer.model.SchoolHoliday;
import com.holidayanalyzer.repository.SchoolHolidayRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/school-holidays")
public class SchoolHolidayController {

    private final SchoolHolidayRepository schoolHolidayRepository;

    public SchoolHolidayController(SchoolHolidayRepository schoolHolidayRepository) {
        this.schoolHolidayRepository = schoolHolidayRepository;
    }

    @GetMapping
    public List<SchoolHoliday> getSchoolHolidays(
            @RequestParam(required = false) String regionCode,
            @RequestParam(required = false) String countryCode,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (regionCode != null && year != null) {
            return schoolHolidayRepository.findByRegionCodeAndYear(regionCode, year);
        } else if (regionCode != null && startDate != null && endDate != null) {
            return schoolHolidayRepository.findByRegionCodeAndDateRange(regionCode, startDate, endDate);
        } else if (countryCode != null && year != null) {
            return schoolHolidayRepository.findByCountryCodeAndYear(countryCode, year);
        }

        return schoolHolidayRepository.findAll();
    }
}
