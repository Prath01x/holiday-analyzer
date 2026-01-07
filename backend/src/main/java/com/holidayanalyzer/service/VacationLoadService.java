package com.holidayanalyzer.service;

import com.holidayanalyzer.dto.VacationLoadResponse;
import com.holidayanalyzer.dto.VacationLoadResponse.WeeklyLoad;
import com.holidayanalyzer.dto.VacationLoadResponse.DailyLoad;
import com.holidayanalyzer.dto.VacationLoadResponse.PeakPeriod;
import com.holidayanalyzer.model.Country;
import com.holidayanalyzer.model.Holiday;
import com.holidayanalyzer.model.SchoolHoliday;
import com.holidayanalyzer.repository.CountryRepository;
import com.holidayanalyzer.repository.HolidayRepository;
import com.holidayanalyzer.repository.SchoolHolidayRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VacationLoadService {

    private final SchoolHolidayRepository schoolHolidayRepository;
    private final HolidayRepository holidayRepository;
    private final CountryRepository countryRepository;

    public VacationLoadService(SchoolHolidayRepository schoolHolidayRepository,
                               HolidayRepository holidayRepository,
                               CountryRepository countryRepository) {
        this.schoolHolidayRepository = schoolHolidayRepository;
        this.holidayRepository = holidayRepository;
        this.countryRepository = countryRepository;
    }

    public VacationLoadResponse calculateVacationLoad(String countryCode, int year) {
        // Hole das Land mit Population
        Country country = countryRepository.findByCode(countryCode)
                .orElseThrow(() -> new IllegalArgumentException("Country not found: " + countryCode));

        List<SchoolHoliday> schoolHolidays = schoolHolidayRepository.findByCountryCodeAndYear(countryCode, year);
        List<Holiday> publicHolidays = holidayRepository.findByCountryCodeAndYear(countryCode, year);

        Map<LocalDate, DailyLoadData> dailyData = new LinkedHashMap<>();

        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);

        for (LocalDate date = startOfYear; !date.isAfter(endOfYear); date = date.plusDays(1)) {
            dailyData.put(date, new DailyLoadData());
        }

        for (SchoolHoliday sh : schoolHolidays) {
            if (sh.getRegion() == null || sh.getRegion().getPopulation() == null) continue;

            long population = sh.getRegion().getPopulation();
            String regionName = sh.getRegion().getName();
            String holidayName = sh.getName();

            for (LocalDate date = sh.getStartDate(); !date.isAfter(sh.getEndDate()); date = date.plusDays(1)) {
                if (dailyData.containsKey(date)) {
                    DailyLoadData data = dailyData.get(date);
                    data.schoolHolidayPopulation += population;
                    data.schoolHolidayDetails.add(regionName + ": " + holidayName);
                }
            }
        }

        for (Holiday h : publicHolidays) {
            if (h.getDate() == null) continue;

            long population;
            if (h.getRegion() != null && h.getRegion().getPopulation() != null) {
                population = h.getRegion().getPopulation();
            } else if (h.getCountry() != null && h.getCountry().getPopulation() != null) {
                population = h.getCountry().getPopulation();
            } else {
                continue;
            }

            LocalDate date = h.getDate();
            if (dailyData.containsKey(date)) {
                DailyLoadData data = dailyData.get(date);
                data.publicHolidayPopulation += population;
                data.publicHolidayDetails.add(h.getLocalName());
            }
        }

        List<WeeklyLoad> weeklyLoads = aggregateByWeek(dailyData, year);
        List<DailyLoad> dailyLoads = convertToDailyLoads(dailyData);
        PeakPeriod peakPeriod = findPeakPeriod(weeklyLoads);

        VacationLoadResponse response = new VacationLoadResponse();
        response.setYear(year);
        response.setCountryPopulation(country.getPopulation());
        response.setWeeklyLoads(weeklyLoads);
        response.setDailyLoads(dailyLoads);
        response.setPeakPeriod(peakPeriod);

        return response;
    }

    private List<WeeklyLoad> aggregateByWeek(Map<LocalDate, DailyLoadData> dailyData, int year) {
        Map<Integer, WeeklyLoad> weekMap = new TreeMap<>();
        WeekFields weekFields = WeekFields.ISO;

        for (Map.Entry<LocalDate, DailyLoadData> entry : dailyData.entrySet()) {
            LocalDate date = entry.getKey();
            DailyLoadData data = entry.getValue();

            int weekNumber = date.get(weekFields.weekOfWeekBasedYear());

            WeeklyLoad week = weekMap.computeIfAbsent(weekNumber, k -> {
                WeeklyLoad w = new WeeklyLoad();
                w.setWeekNumber(k);
                w.setWeekStart(date.with(DayOfWeek.MONDAY));
                w.setWeekEnd(date.with(DayOfWeek.SUNDAY));
                w.setActiveSchoolHolidays(new ArrayList<>());
                w.setActivePublicHolidays(new ArrayList<>());
                return w;
            });

            week.setSchoolHolidayPopulation(Math.max(week.getSchoolHolidayPopulation(), data.schoolHolidayPopulation));
            week.setPublicHolidayPopulation(week.getPublicHolidayPopulation() + data.publicHolidayPopulation);

            for (String detail : data.schoolHolidayDetails) {
                if (!week.getActiveSchoolHolidays().contains(detail)) {
                    week.getActiveSchoolHolidays().add(detail);
                }
            }
            for (String detail : data.publicHolidayDetails) {
                if (!week.getActivePublicHolidays().contains(detail)) {
                    week.getActivePublicHolidays().add(detail);
                }
            }
        }

        for (WeeklyLoad week : weekMap.values()) {
            week.setTotalPopulation(week.getSchoolHolidayPopulation() + week.getPublicHolidayPopulation());
        }

        return new ArrayList<>(weekMap.values());
    }

    private List<DailyLoad> convertToDailyLoads(Map<LocalDate, DailyLoadData> dailyData) {
        return dailyData.entrySet().stream()
                .map(entry -> {
                    DailyLoad dl = new DailyLoad();
                    dl.setDate(entry.getKey());
                    dl.setSchoolHolidayPopulation(entry.getValue().schoolHolidayPopulation);
                    dl.setPublicHolidayPopulation(entry.getValue().publicHolidayPopulation);
                    dl.setTotalPopulation(entry.getValue().schoolHolidayPopulation + entry.getValue().publicHolidayPopulation);
                    return dl;
                })
                .collect(Collectors.toList());
    }

    private PeakPeriod findPeakPeriod(List<WeeklyLoad> weeklyLoads) {
        if (weeklyLoads.isEmpty()) return null;

        WeeklyLoad peakWeek = weeklyLoads.stream()
                .max(Comparator.comparingLong(WeeklyLoad::getSchoolHolidayPopulation))
                .orElse(null);

        if (peakWeek == null) return null;

        int peakStartWeek = peakWeek.getWeekNumber();
        int peakEndWeek = peakWeek.getWeekNumber();
        long threshold = (long) (peakWeek.getSchoolHolidayPopulation() * 0.8);

        for (int i = peakWeek.getWeekNumber() - 1; i >= 1; i--) {
            int weekNum = i;
            WeeklyLoad w = weeklyLoads.stream()
                    .filter(wl -> wl.getWeekNumber() == weekNum)
                    .findFirst().orElse(null);
            if (w != null && w.getSchoolHolidayPopulation() >= threshold) {
                peakStartWeek = i;
            } else {
                break;
            }
        }

        for (int i = peakWeek.getWeekNumber() + 1; i <= 53; i++) {
            int weekNum = i;
            WeeklyLoad w = weeklyLoads.stream()
                    .filter(wl -> wl.getWeekNumber() == weekNum)
                    .findFirst().orElse(null);
            if (w != null && w.getSchoolHolidayPopulation() >= threshold) {
                peakEndWeek = i;
            } else {
                break;
            }
        }

        int finalStartWeek = peakStartWeek;
        int finalEndWeek = peakEndWeek;
        WeeklyLoad startWeekData = weeklyLoads.stream()
                .filter(w -> w.getWeekNumber() == finalStartWeek)
                .findFirst().orElse(peakWeek);
        WeeklyLoad endWeekData = weeklyLoads.stream()
                .filter(w -> w.getWeekNumber() == finalEndWeek)
                .findFirst().orElse(peakWeek);

        Set<String> holidayTypes = new HashSet<>();
        for (WeeklyLoad w : weeklyLoads) {
            if (w.getWeekNumber() >= peakStartWeek && w.getWeekNumber() <= peakEndWeek) {
                for (String sh : w.getActiveSchoolHolidays()) {
                    if (sh.contains("Sommerferien")) holidayTypes.add("Sommerferien");
                    else if (sh.contains("Osterferien")) holidayTypes.add("Osterferien");
                    else if (sh.contains("Herbstferien")) holidayTypes.add("Herbstferien");
                    else if (sh.contains("Weihnachtsferien")) holidayTypes.add("Weihnachtsferien");
                    else if (sh.contains("Winterferien")) holidayTypes.add("Winterferien");
                    else if (sh.contains("Pfingstferien")) holidayTypes.add("Pfingstferien");
                }
            }
        }

        long maxPop = peakWeek.getSchoolHolidayPopulation();
        String description = String.format("Week %d-%d: %.1fM people on %s",
                peakStartWeek, peakEndWeek,
                maxPop / 1_000_000.0,
                String.join(", ", holidayTypes));

        PeakPeriod peak = new PeakPeriod();
        peak.setStartWeek(peakStartWeek);
        peak.setEndWeek(peakEndWeek);
        peak.setStartDate(startWeekData.getWeekStart());
        peak.setEndDate(endWeekData.getWeekEnd());
        peak.setMaxPopulation(maxPop);
        peak.setDescription(description);

        return peak;
    }

    private static class DailyLoadData {
        long schoolHolidayPopulation = 0;
        long publicHolidayPopulation = 0;
        Set<String> schoolHolidayDetails = new HashSet<>();
        Set<String> publicHolidayDetails = new HashSet<>();
    }
}