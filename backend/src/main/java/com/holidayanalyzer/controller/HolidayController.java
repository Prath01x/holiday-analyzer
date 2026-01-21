package com.holidayanalyzer.controller;

import com.holidayanalyzer.model.Holiday;
import com.holidayanalyzer.repository.HolidayRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/holidays")
public class HolidayController {

    private final HolidayRepository holidayRepository;

    public HolidayController(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    @GetMapping
    public List<Holiday> getHolidays(@RequestParam(value = "country", required = false) String countryCode,
                                     @RequestParam(value = "year", required = false) Integer year,
                                     @RequestParam(value = "region", required = false) String regionCode) {

        // If no parameters provided, return all holidays (for admin dashboard)
        if (countryCode == null && year == null) {
            return holidayRepository.findAll();
        }

        // If only country provided, return all holidays for that country
        if (year == null) {
            return holidayRepository.findByCountryCode(countryCode);
        }

        if (regionCode == null || regionCode.isBlank()) {
            // Country-level view: all holidays for that country/year
            return holidayRepository.findByCountryCodeAndYear(countryCode, year);
        }

        // Region-level view: national holidays + regional holidays for that region
        return holidayRepository.findByCountryCodeYearAndRegion(countryCode, year, regionCode);
    }
}
