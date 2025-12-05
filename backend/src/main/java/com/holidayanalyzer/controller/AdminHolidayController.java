package com.holidayanalyzer.controller;

import com.holidayanalyzer.model.Holiday;
import com.holidayanalyzer.service.HolidayImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminHolidayController {

    private final HolidayImportService holidayImportService;

    public AdminHolidayController(HolidayImportService holidayImportService) {
        this.holidayImportService = holidayImportService;
    }

    @PostMapping("/import")
    public ResponseEntity<List<Holiday>> importPublicHolidays(@RequestParam("country") String countryCode,
                                                              @RequestParam("year") int year) {
        List<Holiday> imported = holidayImportService.importPublicHolidays(countryCode, year);
        return ResponseEntity.ok(imported);
    }

    @PostMapping("/import-all")
    public ResponseEntity<Map<String, Integer>> importAllCountries(@RequestParam("year") int year) {
        List<String> countries = List.of("DE", "AT", "CH", "FR", "BE", "NL", "IT");

        Map<String, Integer> summary = new LinkedHashMap<>();
        for (String country : countries) {
            int count = holidayImportService.importPublicHolidays(country, year).size();
            summary.put(country, count);
        }

        return ResponseEntity.ok(summary);
    }
}
