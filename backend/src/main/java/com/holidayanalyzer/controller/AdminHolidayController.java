package com.holidayanalyzer.controller;

import com.holidayanalyzer.model.Country;
import com.holidayanalyzer.model.Holiday;
import com.holidayanalyzer.model.Region;
import com.holidayanalyzer.repository.CountryRepository;
import com.holidayanalyzer.repository.HolidayRepository;
import com.holidayanalyzer.repository.RegionRepository;
import com.holidayanalyzer.service.HolidayImportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminHolidayController {

    private final HolidayImportService holidayImportService;
    private final HolidayRepository holidayRepository;
    private final RegionRepository regionRepository;
    private final CountryRepository countryRepository;

    public AdminHolidayController(HolidayImportService holidayImportService,
                                  HolidayRepository holidayRepository,
                                  RegionRepository regionRepository,
                                  CountryRepository countryRepository) {
        this.holidayImportService = holidayImportService;
        this.holidayRepository = holidayRepository;
        this.regionRepository = regionRepository;
        this.countryRepository = countryRepository;
    }

    @PostMapping("/import")
    public ResponseEntity<List<Holiday>> importPublicHolidays(
            @RequestParam("country") String countryCode,
            @RequestParam("year") int year) {
        List<Holiday> imported = holidayImportService.importPublicHolidays(countryCode, year);
        return ResponseEntity.ok(imported);
    }

    @PostMapping("/import-all")
    public ResponseEntity<Map<String, Integer>> importAllCountries(@RequestParam("year") int year) {
        List<String> countries = List.of("DE", "AT", "CH", "FR", "ES", "NL", "IT");

        Map<String, Integer> summary = new LinkedHashMap<>();
        for (String c : countries) {
            int count = holidayImportService.importPublicHolidays(c, year).size();
            summary.put(c, count);
        }

        return ResponseEntity.ok(summary);
    }

    @PostMapping("/holidays")
    public ResponseEntity<Holiday> addHoliday(
            @RequestParam String name,
            @RequestParam String countryCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String regionCode,
            @RequestParam(required = false) String englishName) {

        // Country-Objekt laden
        Country country = countryRepository.findByCode(countryCode)
                .orElseThrow(() -> new IllegalArgumentException("Unknown country code: " + countryCode));

        Holiday holiday = new Holiday();
        holiday.setLocalName(name);
        holiday.setEnglishName(englishName != null ? englishName : name);
        holiday.setDate(date);
        holiday.setCountryCode(countryCode);
        holiday.setCountry(country);
        holiday.setYear(date.getYear());
        holiday.setTypes("Public");

        if (regionCode != null && !regionCode.isEmpty()) {
            Region region = regionRepository.findByCode(regionCode)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown region code: " + regionCode));
            holiday.setRegion(region);
            holiday.setGlobalHoliday(false);
        } else {
            holiday.setGlobalHoliday(true);
        }

        Holiday saved = holidayRepository.save(holiday);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/holidays/{id}")
    public ResponseEntity<String> deleteHoliday(@PathVariable Long id) {
        holidayRepository.deleteById(id);
        return ResponseEntity.ok("Holiday deleted");
    }
}