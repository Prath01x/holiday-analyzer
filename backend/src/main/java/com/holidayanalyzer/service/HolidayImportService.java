package com.holidayanalyzer.service;

import com.holidayanalyzer.model.Country;
import com.holidayanalyzer.model.Holiday;
import com.holidayanalyzer.repository.CountryRepository;
import com.holidayanalyzer.repository.HolidayRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class HolidayImportService {

    private static final Logger log = LoggerFactory.getLogger(HolidayImportService.class);
    private static final String NAGER_DATE_URL = "https://date.nager.at/api/v3/PublicHolidays/{year}/{countryCode}";

    private final RestTemplate restTemplate;
    private final CountryRepository countryRepository;
    private final HolidayRepository holidayRepository;

    public HolidayImportService(RestTemplate restTemplate,
                                CountryRepository countryRepository,
                                HolidayRepository holidayRepository) {
        this.restTemplate = restTemplate;
        this.countryRepository = countryRepository;
        this.holidayRepository = holidayRepository;
    }

    public List<Holiday> importPublicHolidays(String countryCode, int year) {
        log.info("Importing public holidays from Nager.Date for country={} year={}", countryCode, year);

        Country country = countryRepository.findByCode(countryCode)
            .orElseThrow(() -> new IllegalArgumentException("Unknown country code: " + countryCode));

        NagerPublicHolidayDto[] response = restTemplate.getForObject(
            NAGER_DATE_URL,
            NagerPublicHolidayDto[].class,
            year,
            countryCode
        );

        if (response == null) {
            log.warn("No holidays returned from Nager.Date for country={} year={}", countryCode, year);
            return List.of();
        }

        // Remove old holidays for that country/year to avoid duplicates
        List<Holiday> existing = holidayRepository.findByCountryCodeAndYear(countryCode, year);
        if (!existing.isEmpty()) {
            holidayRepository.deleteAll(existing);
        }

        List<Holiday> toSave = Arrays.stream(response)
            .map(dto -> mapToEntity(dto, country))
            .toList();

        return holidayRepository.saveAll(toSave);
    }

    private Holiday mapToEntity(NagerPublicHolidayDto dto, Country country) {
        Holiday holiday = new Holiday();
        holiday.setCountry(country);
        holiday.setCountryCode(dto.getCountryCode());
        holiday.setDate(LocalDate.parse(dto.getDate()));
        holiday.setLocalName(dto.getLocalName());
        holiday.setEnglishName(dto.getName());
        holiday.setGlobalHoliday(Boolean.TRUE.equals(dto.getGlobal()));
        if (dto.getTypes() != null) {
            holiday.setTypes(String.join(",", dto.getTypes()));
        }

        // Map regional subdivision codes if present (Nager.Date counties/subdivisionCodes)
        if (dto.getCounties() != null) {
            holiday.setSubdivisionCodes(String.join(",", dto.getCounties()));
        }
        holiday.setYear(holiday.getDate().getYear());
        return holiday;
    }

    public static class NagerPublicHolidayDto {
        private String date;
        private String localName;
        private String name;
        private String countryCode;
        private Boolean global;
        private String[] types;
        private String[] counties; // ISO 3166-2 subdivision codes, may be null if not regional

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getLocalName() {
            return localName;
        }

        public void setLocalName(String localName) {
            this.localName = localName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public Boolean getGlobal() {
            return global;
        }

        public void setGlobal(Boolean global) {
            this.global = global;
        }

        public String[] getTypes() {
            return types;
        }

        public void setTypes(String[] types) {
            this.types = types;
        }

        public String[] getCounties() {
            return counties;
        }

        public void setCounties(String[] counties) {
            this.counties = counties;
        }
    }
}
