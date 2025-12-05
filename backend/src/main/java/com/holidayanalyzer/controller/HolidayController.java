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
    public List<Holiday> getHolidays(@RequestParam("country") String countryCode,
                                     @RequestParam("year") int year,
                                     @RequestParam(value = "subdivision", required = false) String subdivision) {

        if (subdivision == null || subdivision.isBlank()) {
            // Country-level view: all holidays for that country/year
            return holidayRepository.findByCountryCodeAndYear(countryCode, year);
        }

        // Region-level view: all global holidays + those that match the subdivision
        return holidayRepository.findByCountryCodeYearAndSubdivision(countryCode, year, subdivision);
    }
}
