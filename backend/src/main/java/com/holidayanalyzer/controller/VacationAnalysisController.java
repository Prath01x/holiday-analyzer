package com.holidayanalyzer.controller;

import com.holidayanalyzer.model.Holiday;
import com.holidayanalyzer.model.SchoolHoliday;
import com.holidayanalyzer.repository.HolidayRepository;
import com.holidayanalyzer.repository.SchoolHolidayRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vacation-analysis")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class VacationAnalysisController {

    private final HolidayRepository holidayRepository;
    private final SchoolHolidayRepository schoolHolidayRepository;

    public VacationAnalysisController(HolidayRepository holidayRepository,
                                      SchoolHolidayRepository schoolHolidayRepository) {
        this.holidayRepository = holidayRepository;
        this.schoolHolidayRepository = schoolHolidayRepository;
    }

    @GetMapping
    public VacationAnalysisResponse getVacationAnalysis(
            @RequestParam String country,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(required = false) String subdivision
    ) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        // Hole alle Jahre im Zeitraum
        Set<Integer> years = new HashSet<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            years.add(date.getYear());
        }

        // Hole Feiertage
        List<Holiday> holidays = new ArrayList<>();
        for (int year : years) {
            holidays.addAll(holidayRepository.findByCountryCodeAndYear(country, year));
        }

        // Filtere nach Zeitraum und optional nach Region
        holidays = holidays.stream()
                .filter(h -> !h.getDate().isBefore(start) && !h.getDate().isAfter(end))
                .filter(h -> subdivision == null || h.getRegion() == null || h.getRegion().getCode().equals(subdivision))
                .collect(Collectors.toList());

        // Hole Schulferien
        List<SchoolHoliday> schoolHolidays = new ArrayList<>();
        for (int year : years) {
            schoolHolidays.addAll(schoolHolidayRepository.findByYearAndCountryCode(year, country));
        }

        // Filtere Schulferien nach Zeitraum und optional nach Region
        schoolHolidays = schoolHolidays.stream()
                .filter(sh -> !sh.getEndDate().isBefore(start) && !sh.getStartDate().isAfter(end))
                .filter(sh -> subdivision == null || sh.getRegion().getCode().equals(subdivision))
                .collect(Collectors.toList());

        VacationAnalysisResponse response = new VacationAnalysisResponse();
        response.setHolidays(holidays);
        response.setSchoolHolidays(schoolHolidays);

        return response;
    }

    public static class VacationAnalysisResponse {
        private List<Holiday> holidays;
        private List<SchoolHoliday> schoolHolidays;

        public List<Holiday> getHolidays() {
            return holidays;
        }

        public void setHolidays(List<Holiday> holidays) {
            this.holidays = holidays;
        }

        public List<SchoolHoliday> getSchoolHolidays() {
            return schoolHolidays;
        }

        public void setSchoolHolidays(List<SchoolHoliday> schoolHolidays) {
            this.schoolHolidays = schoolHolidays;
        }
    }
}