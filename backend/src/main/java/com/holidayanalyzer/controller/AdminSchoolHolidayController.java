package com.holidayanalyzer.controller;

import com.holidayanalyzer.model.Region;
import com.holidayanalyzer.model.SchoolHoliday;
import com.holidayanalyzer.repository.RegionRepository;
import com.holidayanalyzer.repository.SchoolHolidayRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/school-holidays")
public class AdminSchoolHolidayController {

    private final SchoolHolidayRepository schoolHolidayRepository;
    private final RegionRepository regionRepository;

    public AdminSchoolHolidayController(SchoolHolidayRepository schoolHolidayRepository,
                                        RegionRepository regionRepository) {
        this.schoolHolidayRepository = schoolHolidayRepository;
        this.regionRepository = regionRepository;
    }

    @PostMapping
    public ResponseEntity<SchoolHoliday> addSchoolHoliday(
            @RequestParam String name,
            @RequestParam String regionCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam int year) {

        Region region = regionRepository.findByCode(regionCode)
                .orElseThrow(() -> new IllegalArgumentException("Unknown region code: " + regionCode));

        SchoolHoliday schoolHoliday = new SchoolHoliday();
        schoolHoliday.setName(name);
        schoolHoliday.setRegion(region);
        schoolHoliday.setStartDate(startDate);
        schoolHoliday.setEndDate(endDate);
        schoolHoliday.setYear(year);

        return ResponseEntity.ok(schoolHolidayRepository.save(schoolHoliday));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<SchoolHoliday>> addSchoolHolidayBatch(@RequestBody List<SchoolHolidayRequest> requests) {
        List<SchoolHoliday> saved = requests.stream().map(req -> {
            Region region = regionRepository.findByCode(req.regionCode)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown region code: " + req.regionCode));

            SchoolHoliday sh = new SchoolHoliday();
            sh.setName(req.name);
            sh.setRegion(region);
            sh.setStartDate(req.startDate);
            sh.setEndDate(req.endDate);
            sh.setYear(req.year);
            return schoolHolidayRepository.save(sh);
        }).toList();

        return ResponseEntity.ok(saved);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteByRegionAndYear(
            @RequestParam String regionCode,
            @RequestParam int year) {
        List<SchoolHoliday> toDelete = schoolHolidayRepository.findByRegionCodeAndYear(regionCode, year);
        schoolHolidayRepository.deleteAll(toDelete);
        return ResponseEntity.ok("Deleted " + toDelete.size() + " school holidays for " + regionCode + " " + year);
    }

    public static class SchoolHolidayRequest {
        public String name;
        public String regionCode;
        public LocalDate startDate;
        public LocalDate endDate;
        public int year;
    }
}
