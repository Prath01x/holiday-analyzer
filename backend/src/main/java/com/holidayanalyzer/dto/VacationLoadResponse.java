package com.holidayanalyzer.dto;

import java.time.LocalDate;
import java.util.List;

public class VacationLoadResponse {

    private int year;
    private long countryPopulation;
    private List<WeeklyLoad> weeklyLoads;
    private List<DailyLoad> dailyLoads;
    private PeakPeriod peakPeriod;

    public VacationLoadResponse() {}

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public long getCountryPopulation() { return countryPopulation; }
    public void setCountryPopulation(long countryPopulation) { this.countryPopulation = countryPopulation; }

    public List<WeeklyLoad> getWeeklyLoads() { return weeklyLoads; }
    public void setWeeklyLoads(List<WeeklyLoad> weeklyLoads) { this.weeklyLoads = weeklyLoads; }

    public List<DailyLoad> getDailyLoads() { return dailyLoads; }
    public void setDailyLoads(List<DailyLoad> dailyLoads) { this.dailyLoads = dailyLoads; }

    public PeakPeriod getPeakPeriod() { return peakPeriod; }
    public void setPeakPeriod(PeakPeriod peakPeriod) { this.peakPeriod = peakPeriod; }

    public static class WeeklyLoad {
        private int weekNumber;
        private LocalDate weekStart;
        private LocalDate weekEnd;
        private long schoolHolidayPopulation;
        private long publicHolidayPopulation;
        private long totalPopulation;
        private List<String> activeSchoolHolidays;
        private List<String> activePublicHolidays;

        public WeeklyLoad() {}

        public int getWeekNumber() { return weekNumber; }
        public void setWeekNumber(int weekNumber) { this.weekNumber = weekNumber; }

        public LocalDate getWeekStart() { return weekStart; }
        public void setWeekStart(LocalDate weekStart) { this.weekStart = weekStart; }

        public LocalDate getWeekEnd() { return weekEnd; }
        public void setWeekEnd(LocalDate weekEnd) { this.weekEnd = weekEnd; }

        public long getSchoolHolidayPopulation() { return schoolHolidayPopulation; }
        public void setSchoolHolidayPopulation(long schoolHolidayPopulation) { this.schoolHolidayPopulation = schoolHolidayPopulation; }

        public long getPublicHolidayPopulation() { return publicHolidayPopulation; }
        public void setPublicHolidayPopulation(long publicHolidayPopulation) { this.publicHolidayPopulation = publicHolidayPopulation; }

        public long getTotalPopulation() { return totalPopulation; }
        public void setTotalPopulation(long totalPopulation) { this.totalPopulation = totalPopulation; }

        public List<String> getActiveSchoolHolidays() { return activeSchoolHolidays; }
        public void setActiveSchoolHolidays(List<String> activeSchoolHolidays) { this.activeSchoolHolidays = activeSchoolHolidays; }

        public List<String> getActivePublicHolidays() { return activePublicHolidays; }
        public void setActivePublicHolidays(List<String> activePublicHolidays) { this.activePublicHolidays = activePublicHolidays; }
    }

    public static class DailyLoad {
        private LocalDate date;
        private long schoolHolidayPopulation;
        private long publicHolidayPopulation;
        private long totalPopulation;

        public DailyLoad() {}

        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }

        public long getSchoolHolidayPopulation() { return schoolHolidayPopulation; }
        public void setSchoolHolidayPopulation(long schoolHolidayPopulation) { this.schoolHolidayPopulation = schoolHolidayPopulation; }

        public long getPublicHolidayPopulation() { return publicHolidayPopulation; }
        public void setPublicHolidayPopulation(long publicHolidayPopulation) { this.publicHolidayPopulation = publicHolidayPopulation; }

        public long getTotalPopulation() { return totalPopulation; }
        public void setTotalPopulation(long totalPopulation) { this.totalPopulation = totalPopulation; }
    }

    public static class PeakPeriod {
        private int startWeek;
        private int endWeek;
        private LocalDate startDate;
        private LocalDate endDate;
        private long maxPopulation;
        private String description;

        public PeakPeriod() {}

        public int getStartWeek() { return startWeek; }
        public void setStartWeek(int startWeek) { this.startWeek = startWeek; }

        public int getEndWeek() { return endWeek; }
        public void setEndWeek(int endWeek) { this.endWeek = endWeek; }

        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

        public long getMaxPopulation() { return maxPopulation; }
        public void setMaxPopulation(long maxPopulation) { this.maxPopulation = maxPopulation; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}